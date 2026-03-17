package com.example.petpassport_android_app.presentation.screens.petProfile


import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.petpassport_android_app.BuildConfig
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
        data class Edit(val pet: Pet, val isUploadingPhoto: Boolean = false) : State()
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

    fun uploadPhoto(petId: Int, imageBytes: ByteArray?) {
        if (imageBytes == null || imageBytes.isEmpty()) return

        screenModelScope.launch {
            val currentState = _state.value
            if (currentState !is State.Edit) return@launch

            _state.value = currentState.copy(isUploadingPhoto = true)

            val result = petRepository.uploadPetPhoto(petId, imageBytes)

            result.onSuccess { photoDto ->
                val baseUrl = BuildConfig.BASE_URL
                val rawUrl = photoDto.url


                val cleanPath = if (rawUrl?.startsWith("/") == true) rawUrl.substring(1) else rawUrl
                val fullUrl = if (cleanPath.isNullOrBlank()) "" else {
                    if (baseUrl.endsWith("/")) "$baseUrl$cleanPath" else "$baseUrl/$cleanPath"
                }


                val freshUrlWithTimestamp = if (fullUrl.isNotEmpty()) {
                    "$fullUrl?t=${System.currentTimeMillis()}"
                } else ""

                Log.d("PetModel", "Фото загружено. Итоговый URL: $freshUrlWithTimestamp")


                val updatedPet = currentState.pet.copy(photoUrl = freshUrlWithTimestamp)
                _state.value = State.Edit(updatedPet, isUploadingPhoto = false)

            }.onFailure { e ->
                Log.e("PetModel", "Ошибка загрузки: ${e.message}")
                _state.value = State.Error("Ошибка загрузки фото: ${e.message}")
            }
        }
    }
}



