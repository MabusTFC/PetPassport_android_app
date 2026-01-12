package com.example.petpassport_android_app.presentation.screens.petProfile


import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import dagger.assisted.Assisted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PetProfileScreenModel @Inject constructor(
    private val petRepository: PetRepository
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class Success(val pet: Pet) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun loadPetById(petId: Int) {
        screenModelScope.launch {
            try {
                val pet = petRepository.getPetById(petId)
                if (pet == null) {
                    _state.value = State.Error("Питомец не найден")
                } else {
                    _state.value = State.Success(pet)
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки")
            }
        }
    }
}

