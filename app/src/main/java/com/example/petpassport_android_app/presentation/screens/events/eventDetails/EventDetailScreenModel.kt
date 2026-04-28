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
import com.example.petpassport_android_app.notification.rescheduleFromModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventDetailScreenModel @Inject constructor(
    private val repository: EventMedicineRepository,
    private val eventReminderRepository: EventReminderRepository,
    private val notificationPrefs: NotificationPreferencesRepository,
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
            val st = eventReminderRepository.getState(event.id)
            val cancel = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                previouslyStoredOffsets = st?.offsetsMinutes.orEmpty(),
                modelOffsets = event.reminderOffsetsMinutes,
            )
            NotificationScheduler.cancelAll(context, event.id, cancel)
            eventReminderRepository.remove(event.id)
            val success = when (event) {
                is Vaccine -> repository.deleteVaccine(event.id)
                is Treatment -> repository.deleteTreatment(event.id)
                is DoctorVisit -> repository.deleteDoctorVisit(event.id)
            }
            _state.value = if (success) State.Deleted else State.Error("Ошибка удаления")
        }
    }

    fun updateEvent(context: Context, event: PetEvent) {
        screenModelScope.launch {
            _state.value = State.Loading
            val previous = eventReminderRepository.getState(event.id)
            val success = when (event) {
                is Vaccine -> repository.updateVaccine(event.id, event)
                is Treatment -> repository.updateTreatment(event.id, event)
                is DoctorVisit -> repository.updateDoctorVisit(event.id, event)
            }
            if (success) {
                val cancel = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
                    previouslyStoredOffsets = previous?.offsetsMinutes.orEmpty(),
                    modelOffsets = event.reminderOffsetsMinutes + (previous?.offsetsMinutes.orEmpty()),
                )
                rescheduleFromModel(
                    context = context,
                    event = event,
                    eventReminderRepository = eventReminderRepository,
                    notificationPreferencesRepository = notificationPrefs,
                    cancelOffsets = cancel,
                )
            }
            _state.value = if (success) State.Saved else State.Error("Ошибка сохранения")
        }
    }
}
