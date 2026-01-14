package com.example.petpassport_android_app.presentation.screens.events

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Event.*
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsScreenModel @Inject constructor(
    private val repository: EventMedicineRepository
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class Success(val events: List<PetEvent>) : State()
        data class Error(val message: String) : State()
        object Saving : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun loadEvents(petId: Int) {
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val events = repository.getPetEvents(petId)
                _state.value = State.Success(events)
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки процедур")
            }
        }
    }

    fun addVaccine(vaccine: Vaccine, petId: Int) {
        screenModelScope.launch {
            _state.value = State.Saving
            repository.addVaccine(vaccine)
            loadEvents(petId)
        }
    }

    fun addTreatment(treatment: Treatment, petId: Int) {
        screenModelScope.launch {
            _state.value = State.Saving
            repository.addTreatment(treatment)
            loadEvents(petId)
        }
    }

    fun addDoctorVisit(visit: DoctorVisit, petId: Int) {
        screenModelScope.launch {
            _state.value = State.Saving
            repository.addDoctorVisit(visit)
            loadEvents(petId)
        }
    }
}
