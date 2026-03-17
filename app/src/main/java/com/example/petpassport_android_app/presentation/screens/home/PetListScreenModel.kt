package com.example.petpassport_android_app.presentation.screens.home

import android.content.SharedPreferences
import android.util.Log
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

            try {
                val ownerId = sharedPreferences.getInt("owner_id", -1)
                if (ownerId == -1) return@launch

                val petsFromServer = repository.getPetsByOwner(ownerId)

                val currentState = _state.value
                val existingPhotos = if (currentState is PetsState.Success) {
                    currentState.pets.associate { it.id to it.photoUrl }
                } else emptyMap()

                val mergedPets = petsFromServer.map { serverPet ->
                    val cachedPhoto = existingPhotos[serverPet.id]
                    if (!cachedPhoto.isNullOrBlank()) {
                        serverPet.copy(photoUrl = cachedPhoto)
                    } else {
                        serverPet
                    }
                }

                _state.value = if (mergedPets.isEmpty()) PetsState.Empty else PetsState.Success(mergedPets)

                mergedPets.forEach { pet ->
                    refreshPet(pet.id)
                }
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
            // Карта состояний карточки
            _petStates.value = _petStates.value.toMutableMap().apply {
                put(petId, PetCardState.Loading)
            }

            try {
                val freshPet = petRepository.getPetById(petId)
                if (freshPet != null) {
                    val finalUrl = if (!freshPet.photoUrl.isNullOrBlank()) {
                        val separator = if (freshPet.photoUrl!!.contains("?")) "&" else "?"
                        "${freshPet.photoUrl}$separator${System.currentTimeMillis()}"
                    } else null

                    val petWithPhoto = freshPet.copy(photoUrl = finalUrl)

                    // 1. Обновляем карту карточек
                    _petStates.value = _petStates.value.toMutableMap().apply {
                        put(petId, PetCardState.Success(petWithPhoto))
                    }

                    // 2. ОБЯЗАТЕЛЬНО обновляем основной список
                    val currentState = _state.value
                    if (currentState is PetsState.Success) {
                        val updatedList = currentState.pets.map {
                            if (it.id == petId) petWithPhoto else it
                        }
                        _state.value = PetsState.Success(updatedList)
                    }
                }
            } catch (e: Exception) {
                _petStates.value = _petStates.value.toMutableMap().apply {
                    put(petId, PetCardState.Error("Ошибка"))
                }
            }
        }
    }




}