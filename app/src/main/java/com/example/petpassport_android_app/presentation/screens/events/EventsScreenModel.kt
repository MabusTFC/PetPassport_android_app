package com.example.petpassport_android_app.presentation.screens.events

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.domain.model.Event.EventReminderUiPayload
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import com.example.petpassport_android_app.notification.EventNotificationPlanner
import com.example.petpassport_android_app.notification.NotificationScheduler
import com.example.petpassport_android_app.notification.syncPerEventReminderFromBell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventsScreenModel @Inject constructor(
    private val repository: EventMedicineRepository,
    private val notificationPrefs: NotificationPreferencesRepository,
    private val eventReminderRepository: EventReminderRepository,
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class Success(
            val events: List<PetEvent>,
        ) : State()
        data class Error(val message: String) : State()
        object Saving : State()
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

    fun loadEvents(petId: Int) {
        lastPetId = petId
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val events = repository.getPetEvents(petId)
                val today = LocalDate.now()
                val threeDaysAgo = today.minusDays(3)

                val visibleEvents = events.filter { event ->
                    try {
                        val datePart = event.date.substringBefore("T")
                        val date = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE)
                        !date.isBefore(threeDaysAgo)
                    } catch (e: Exception) {
                        false
                    }
                }.sortedBy { it.date.substringBefore("T") }

                _state.value = State.Success(visibleEvents)
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки процедур")
            }
        }
    }

    fun toggleNotifications(context: Context, enabled: Boolean) {
        screenModelScope.launch {
            notificationPrefs.setNotificationsEnabled(enabled)
            val currentState = _state.value as? State.Success ?: return@launch
            val store = eventReminderRepository.snapshot()
            if (!enabled) {
                currentState.events.forEach { event ->
                    val st = store[event.id]
                    val cancel = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                        previouslyStoredOffsets = st?.offsetsMinutes.orEmpty(),
                        modelOffsets = event.reminderOffsetsMinutes,
                    )
                    NotificationScheduler.cancelAll(context, event.id, cancel)
                }
            } else {
                val global = true
                currentState.events.forEach { event ->
                    val cancel = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                        previouslyStoredOffsets = store[event.id]?.offsetsMinutes.orEmpty(),
                        modelOffsets = event.reminderOffsetsMinutes,
                    )
                    EventNotificationPlanner.reschedule(
                        context = context,
                        event = event,
                        globalNotificationsEnabled = global,
                        cancelOffsets = cancel,
                    )
                }
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
            lastPetId?.let { loadEvents(it) }
        }
    }

    fun addVaccine(context: Context, vaccine: Vaccine, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            _state.value = State.Saving
            val newId = repository.addVaccine(vaccine.copy(petId = petId)) ?: run {
                loadEvents(petId)
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
            val cancel = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                previouslyStoredOffsets = emptyList(),
                modelOffsets = reminder.offsetsMinutes,
            )
            EventNotificationPlanner.reschedule(
                context = context,
                event = saved,
                globalNotificationsEnabled = global,
                cancelOffsets = cancel,
            )
            loadEvents(petId)
        }
    }

    fun addTreatment(context: Context, treatment: Treatment, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            _state.value = State.Saving
            val newId = repository.addTreatment(treatment.copy(petId = petId)) ?: run {
                loadEvents(petId)
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
                    emptyList(),
                    reminder.offsetsMinutes,
                ),
            )
            loadEvents(petId)
        }
    }

    fun addDoctorVisit(context: Context, visit: DoctorVisit, petId: Int, reminder: EventReminderUiPayload) {
        screenModelScope.launch {
            _state.value = State.Saving
            val newId = repository.addDoctorVisit(visit.copy(petId = petId)) ?: run {
                loadEvents(petId)
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
                    emptyList(),
                    reminder.offsetsMinutes,
                ),
            )
            loadEvents(petId)
        }
    }

    fun completeEvent(event: PetEvent, petId: Int) {
        screenModelScope.launch {
            _state.value = State.Saving
            try {
                loadEvents(petId)
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка")
            }
        }
    }
}
