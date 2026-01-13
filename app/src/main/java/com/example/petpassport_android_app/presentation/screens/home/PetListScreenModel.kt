package com.example.petpassport_android_app.presentation.screens.home

import android.content.SharedPreferences
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class PetListScreenModel @Inject constructor(
    val repository: OwnerRepository,
    val petRepository: com.example.petpassport_android_app.domain.repository.PetRepository,
    val sharedPreferences: SharedPreferences
): ScreenModel {

    sealed class PetsState(){
        object Loading: PetsState()
        object Empty: PetsState()
        data class Success(val pets: List<Pet>): PetsState()
        data class Error(val mess: String): PetsState()
    }

    private val _state = MutableStateFlow<PetsState>(PetsState.Loading)
    val state = _state.asStateFlow()

    init {
        loadPets()
    }

    fun retry() {
        loadPets()
    }

    private fun loadPets() {
        screenModelScope.launch {
            _state.value = PetsState.Loading
            try {

                val ownerId = sharedPreferences.getInt("owner_id", -1)
                if (ownerId == -1) {
                    _state.value = PetsState.Error("Не авторизован")
                    return@launch
                }

                val pets = repository.getPetsByOwner(ownerId)

                _state.value = if (pets.isEmpty()) PetsState.Empty else PetsState.Success(pets)
            } catch (e: Exception) {
                _state.value = PetsState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun addPet(pet: Pet) {
        screenModelScope.launch {
            try {
                petRepository.createPet(pet)
                loadPets()
            } catch (e: Exception) {
                _state.value = PetsState.Error("Ошибка сохранения питомца")
            }
        }
    }

}

