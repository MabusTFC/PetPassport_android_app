package com.example.petpassport_android_app.presentation.screens.petProfile

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.BuildConfig
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import com.example.petpassport_android_app.domain.repository.PetRepository
import com.example.petpassport_android_app.notification.syncPerEventReminderFromBell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.EventReminderUiPayload
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.notification.EventNotificationPlanner
import kotlinx.coroutines.flow.first

class PetProfileScreenModel @Inject constructor(
    private val petRepository: PetRepository,
    private val eventRepository: EventMedicineRepository,
    private val notificationPrefs: NotificationPreferencesRepository,
    private val eventReminderRepository: EventReminderRepository,
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class View(val pet: Pet, val events: List<PetEvent> = emptyList()) : State()
        data class Edit(
            val pet: Pet,
            val events: List<PetEvent> = emptyList(),
            val isUploadingPhoto: Boolean = false
        ) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(true)
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()

    private var lastPetId: Int? = null

    init {
        screenModelScope.launch {
            notificationPrefs.isNotificationsEnabled.collect { enabled ->
                _isNotificationsEnabled.value = enabled
            }
        }
    }

    fun loadPetById(petId: Int) {
        lastPetId = petId
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val pet = petRepository.getPetById(petId)
                val allEvents = eventRepository.getPetEvents(petId)

                val today = LocalDate.now()
                val upcomingEvents = allEvents.filter { event ->
                    try {
                        val datePart = event.date.substringBefore("T")
                        val date = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE)
                        !date.isBefore(today)
                    } catch (e: Exception) {
                        false
                    }
                }

                if (pet != null) {
                    _state.value = State.View(pet, upcomingEvents)
                } else {
                    _state.value = State.Error("Питомец не найден")
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun dismissEditMode() {
        val current = _state.value
        if (current is State.Edit) {
            _state.value = State.View(current.pet, current.events)
        }
    }

    fun enableEditMode() {
        val current = _state.value
        if (current is State.View) {
            _state.value = State.Edit(current.pet, current.events)
        }
    }

    fun savePet(updatedPet: Pet) {
        screenModelScope.launch {
            val currentEvents = (state.value as? State.Edit)?.events ?: emptyList()
            petRepository.updatePet(updatedPet.id, updatedPet)
            _state.value = State.View(updatedPet, currentEvents)
        }
    }

    fun uploadPhoto(petId: Int, imageBytes: ByteArray?) {
        if (imageBytes == null || imageBytes.isEmpty()) return
        screenModelScope.launch {
            val currentState = _state.value as? State.Edit ?: return@launch
            _state.value = currentState.copy(isUploadingPhoto = true)

            val result = petRepository.uploadPetPhoto(petId, imageBytes)
            result.onSuccess { photoDto ->
                val baseUrl = BuildConfig.BASE_URL
                val rawUrl = photoDto.url
                val cleanPath = if (rawUrl?.startsWith("/") == true) rawUrl.substring(1) else rawUrl
                val fullUrl = if (cleanPath.isNullOrBlank()) "" else "$baseUrl/$cleanPath"
                val freshUrl = if (fullUrl.isNotEmpty()) "$fullUrl?t=${System.currentTimeMillis()}" else ""

                val updatedPet = currentState.pet.copy(photoUrl = freshUrl)
                _state.value = State.Edit(updatedPet, currentState.events, isUploadingPhoto = false)
            }.onFailure { e ->
                _state.value = State.Error("Ошибка загрузки фото: ${e.message}")
            }
        }
    }

    fun onEventReminderToggle(context: Context, event: PetEvent, enabled: Boolean) {
        screenModelScope.launch {
            syncPerEventReminderFromBell(
                context = context,
                event = event,
                enabled = enabled,
                eventReminderRepository = eventReminderRepository,
                notificationPreferencesRepository = notificationPrefs,
            )
            lastPetId?.let { loadPetById(it) }
        }
    }


    fun addVaccine(context: Context, vaccine: Vaccine, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            val newId = eventRepository.addVaccine(vaccine.copy(petId = petId)) ?: run {
                loadPetById(petId)
                return@launch
            }
            val saved = vaccine.copy(
                id = newId,
                petId = petId,
                reminderEnabled = reminder.enabled,
                reminderOffsetsMinutes = reminder.offsetsMinutes,
            )
            val global = notificationPrefs.isNotificationsEnabled.first()
            eventReminderRepository.setState(
                eventId = newId,
                enabled = reminder.enabled,
                offsetsMinutes = reminder.offsetsMinutes,
                titleForNotification = saved.title,
                eventDateIso = saved.date,
            )
            EventNotificationPlanner.reschedule(
                context = context,
                event = saved,
                globalNotificationsEnabled = global,
                cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                    emptyList(), reminder.offsetsMinutes
                ),
            )
            loadPetById(petId)
        }
    }

    fun addTreatment(context: Context, treatment: Treatment, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            val newId = eventRepository.addTreatment(treatment.copy(petId = petId)) ?: run {
                loadPetById(petId)
                return@launch
            }
            val saved = treatment.copy(
                id = newId,
                petId = petId,
                reminderEnabled = reminder.enabled,
                reminderOffsetsMinutes = reminder.offsetsMinutes,
            )
            val global = notificationPrefs.isNotificationsEnabled.first()
            eventReminderRepository.setState(
                eventId = newId,
                enabled = reminder.enabled,
                offsetsMinutes = reminder.offsetsMinutes,
                titleForNotification = saved.title,
                eventDateIso = saved.date,
            )
            EventNotificationPlanner.reschedule(
                context = context,
                event = saved,
                globalNotificationsEnabled = global,
                cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                    emptyList(), reminder.offsetsMinutes
                ),
            )
            loadPetById(petId)
        }
    }

    fun addDoctorVisit(context: Context, visit: DoctorVisit, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            val newId = eventRepository.addDoctorVisit(visit.copy(petId = petId)) ?: run {
                loadPetById(petId)
                return@launch
            }
            val saved = visit.copy(
                id = newId,
                petId = petId,
                reminderEnabled = reminder.enabled,
                reminderOffsetsMinutes = reminder.offsetsMinutes,
            )
            val global = notificationPrefs.isNotificationsEnabled.first()
            eventReminderRepository.setState(
                eventId = newId,
                enabled = reminder.enabled,
                offsetsMinutes = reminder.offsetsMinutes,
                titleForNotification = saved.title,
                eventDateIso = saved.date,
            )
            EventNotificationPlanner.reschedule(
                context = context,
                event = saved,
                globalNotificationsEnabled = global,
                cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                    emptyList(), reminder.offsetsMinutes
                ),
            )
            loadPetById(petId)
        }
    }

}
