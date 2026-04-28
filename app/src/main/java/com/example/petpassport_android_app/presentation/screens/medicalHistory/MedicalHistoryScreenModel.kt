package com.example.petpassport_android_app.presentation.screens.medicalHistory

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import com.example.petpassport_android_app.notification.syncPerEventReminderFromBell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MedicalHistoryScreenModel @Inject constructor(
    private val eventMedicineRepository: EventMedicineRepository,
    private val notificationPrefs: NotificationPreferencesRepository,
    private val eventReminderRepository: EventReminderRepository,
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class Success(val events: List<PetEvent>) : State()
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

    fun loadHistory(petId: Int) {
        lastPetId = petId
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val events = eventMedicineRepository.getPetEvents(petId)
                val today = LocalDate.now()

                val pastEvents = events.filter { event ->
                    try {
                        val datePart = event.date.substringBefore("T")
                        val date = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE)
                        date.isBefore(today)
                    } catch (e: Exception) {
                        false
                    }
                }.sortedByDescending { it.date }

                _state.value = State.Success(pastEvents)
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки истории")
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
            lastPetId?.let { loadHistory(it) }
        }
    }
}
