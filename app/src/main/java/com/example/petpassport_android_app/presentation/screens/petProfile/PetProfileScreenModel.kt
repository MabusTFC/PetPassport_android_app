package com.example.petpassport_android_app.presentation.screens.petProfile

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.BuildConfig
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.repository.PetRepository
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PetProfileScreenModel @Inject constructor(
    private val petRepository: PetRepository,
    private val eventRepository: EventMedicineRepository // Внедряем новый репозиторий
) : ScreenModel {

    sealed class State {
        object Loading : State()
        data class View(val pet: Pet, val events: List<PetEvent> = emptyList()) : State()
        data class Edit(
            val pet: Pet,
            val events: List<PetEvent> = emptyList(), // Держим события и в режиме редактирования для фона
            val isUploadingPhoto: Boolean = false
        ) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    fun loadPetById(petId: Int) {
        screenModelScope.launch {
            _state.value = State.Loading
            try {
                val pet = petRepository.getPetById(petId)
                val events = eventRepository.getPetEvents(petId) // Запрос к твоему новому репозиторию

                if (pet != null) {
                    _state.value = State.View(pet, events)
                } else {
                    _state.value = State.Error("Питомец не найден")
                }
            } catch (e: Exception) {
                _state.value = State.Error("Ошибка загрузки: ${e.message}")
            }
        }
    }

    fun dismissEditMode() {
        val current = _state.value
        if (current is State.Edit) {
            _state.value = State.View(current.pet, current.events)
        }
    }

    fun enableEditMode() {
        val current = _state.value
        if (current is State.View) {
            _state.value = State.Edit(current.pet, current.events)
        }
    }

    fun savePet(updatedPet: Pet) {
        screenModelScope.launch {
            val currentEvents = (state.value as? State.Edit)?.events ?: emptyList()
            petRepository.updatePet(updatedPet.id, updatedPet)
            _state.value = State.View(updatedPet, currentEvents)
        }
    }

    fun uploadPhoto(petId: Int, imageBytes: ByteArray?) {
        if (imageBytes == null || imageBytes.isEmpty()) return
        screenModelScope.launch {
            val currentState = _state.value as? State.Edit ?: return@launch
            _state.value = currentState.copy(isUploadingPhoto = true)

            val result = petRepository.uploadPetPhoto(petId, imageBytes)
            result.onSuccess { photoDto ->
                val baseUrl = BuildConfig.BASE_URL
                val rawUrl = photoDto.url
                val cleanPath = if (rawUrl?.startsWith("/") == true) rawUrl.substring(1) else rawUrl
                val fullUrl = if (cleanPath.isNullOrBlank()) "" else "$baseUrl/$cleanPath"
                val freshUrl = if (fullUrl.isNotEmpty()) "$fullUrl?t=${System.currentTimeMillis()}" else ""

                val updatedPet = currentState.pet.copy(photoUrl = freshUrl)
                _state.value = State.Edit(updatedPet, currentState.events, isUploadingPhoto = false)
            }.onFailure { e ->
                _state.value = State.Error("Ошибка загрузки фото: ${e.message}")
            }
        }
    }
}