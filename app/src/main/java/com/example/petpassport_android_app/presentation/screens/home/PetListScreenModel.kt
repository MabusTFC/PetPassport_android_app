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

    sealed class PetCardState {
        object Loading : PetCardState()
        data class Success(val pet: Pet) : PetCardState()
        data class Error(val message: String) : PetCardState()
    }


    private val _state = MutableStateFlow<PetsState>(PetsState.Loading)
    val state = _state.asStateFlow()
    private val _petStates = MutableStateFlow<Map<Int, PetCardState>>(emptyMap())
    val petStates = _petStates.asStateFlow()

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

    fun refreshPet(petId: Int) {
        screenModelScope.launch {
            _petStates.value = _petStates.value.toMutableMap().apply {
                put(petId, PetCardState.Loading)
            }

            try {
                val pet = petRepository.getPetById(petId)
                _petStates.value = _petStates.value.toMutableMap().apply {
                    if (pet != null) put(petId, PetCardState.Success(pet))
                    else put(petId, PetCardState.Error("Ошибка загрузки питомца"))
                }
            } catch (e: Exception) {
                _petStates.value = _petStates.value.toMutableMap().apply {
                    put(petId, PetCardState.Error(e.message ?: "Ошибка"))
                }
            }
        }
    }




}

