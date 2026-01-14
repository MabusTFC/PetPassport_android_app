package com.example.petpassport_android_app.presentation.screens.petProfile


import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PetProfileScreenModel @Inject constructor(
    private val petRepository: PetRepository
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class View(val pet: Pet) : State()
        data class Edit(val pet: Pet) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun loadPetById(petId: Int) {
        screenModelScope.launch {
            val pet = petRepository.getPetById(petId)
            if (pet != null) {
                _state.value = State.View(pet)
            } else {
                _state.value = State.Error("Питомец не найден")
            }
        }
    }

    fun enableEditMode() {
        val current = _state.value
        if (current is State.View) {
            _state.value = State.Edit(current.pet)
        }
    }

    fun savePet(updatedPet: Pet) {
        screenModelScope.launch {
            petRepository.updatePet(updatedPet.id, updatedPet)
            _state.value = State.View(updatedPet)
        }
    }
}



