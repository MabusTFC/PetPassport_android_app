package com.example.petpassport_android_app.presentation.screens.events.eventDetails

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import com.example.petpassport_android_app.notification.EventNotificationPlanner
import com.example.petpassport_android_app.notification.NotificationScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class EventDetailScreenModel @Inject constructor(
    private val repository: EventMedicineRepository,
    private val notificationPrefs: NotificationPreferencesRepository,
    private val eventReminderRepository: EventReminderRepository,
) : ScreenModel {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Deleted : State()
        object Saved : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    fun deleteEvent(context: Context, event: PetEvent) {
        screenModelScope.launch {
            _state.value = State.Loading
            // Отменяем все уведомления для этого события
            NotificationScheduler.cancelAll(context, event.id)
            val success = when (event) {
                is Vaccine -> repository.deleteVaccine(event.id)
                is Treatment -> repository.deleteTreatment(event.id)
                is DoctorVisit -> repository.deleteDoctorVisit(event.id)
            }
            if (success) {
                eventReminderRepository.removeState(event.id)
            }
            _state.value = if (success) State.Deleted else State.Error("Ошибка удаления")
        }
    }

    fun updateEvent(context: Context, event: PetEvent) {
        screenModelScope.launch {
            _state.value = State.Loading
            val previous = eventReminderRepository.snapshot()[event.id]
            // Отменяем старые уведомления перед обновлением
            val cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                previouslyStoredOffsets = previous?.offsetsMinutes.orEmpty(),
                modelOffsets = event.reminderOffsetsMinutes,
            )
            NotificationScheduler.cancelAll(context, event.id, cancelOffsets)
            val success = when (event) {
                is Vaccine -> repository.updateVaccine(event.id, event)
                is Treatment -> repository.updateTreatment(event.id, event)
                is DoctorVisit -> repository.updateDoctorVisit(event.id, event)
            }
            if (success) {
                eventReminderRepository.setState(
                    eventId = event.id,
                    enabled = event.reminderEnabled,
                    offsetsMinutes = event.reminderOffsetsMinutes,
                    titleForNotification = event.title,
                    eventDateIso = event.date,
                )
                EventNotificationPlanner.reschedule(
                    context = context,
                    event = event,
                    globalNotificationsEnabled = notificationPrefs.isNotificationsEnabled.first(),
                    cancelOffsets = cancelOffsets,
                )
            }
            _state.value = if (success) State.Saved else State.Error("Ошибка сохранения")
        }
    }
}
