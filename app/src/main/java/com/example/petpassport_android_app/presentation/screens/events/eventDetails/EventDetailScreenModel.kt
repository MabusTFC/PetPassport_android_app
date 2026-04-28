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
    private val repository: EventMedicineRepository
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
            _state.value = if (success) State.Deleted else State.Error("Ошибка удаления")
        }
    }

    fun updateEvent(context: Context, event: PetEvent) {
        screenModelScope.launch {
            _state.value = State.Loading
            // Отменяем старые уведомления перед обновлением
            NotificationScheduler.cancelAll(context, event.id)
            val success = when (event) {
                is Vaccine -> repository.updateVaccine(event.id, event)
                is Treatment -> repository.updateTreatment(event.id, event)
                is DoctorVisit -> repository.updateDoctorVisit(event.id, event)
            }
            _state.value = if (success) State.Saved else State.Error("Ошибка сохранения")
        }
    }
}
