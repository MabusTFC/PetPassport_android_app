package com.example.petpassport_android_app.presentation.screens.medicalHistory

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.PetRepository
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MedicalHistoryScreenModel @Inject constructor(
    private val petRepository: PetRepository,
    private val eventMedicineRepository: EventMedicineRepository
) : ScreenModel {

    sealed class State {

        object Loading : State()

        data class Success(val pet: Pet) : State()

        data class Error(val message: String) : State()

        object Saving : State()
    }

    private val _state = MutableStateFlow<PetProfileScreenModel.State>(PetProfileScreenModel.State.Loading)

    val state = _state.asStateFlow()

    fun loadPetById(petId: Int) {
        screenModelScope.launch {
            _state.value = PetProfileScreenModel.State.Loading
            try {
                val pet = petRepository.getPetById(petId)
                if (pet == null) {
                    _state.value = PetProfileScreenModel.State.Error("Питомец не найден")
                } else {
                    _state.value = PetProfileScreenModel.State.Success(pet)
                }
            } catch (e: Exception) {
                _state.value = PetProfileScreenModel.State.Error("Ошибка загрузки")
            }
        }
    }


}