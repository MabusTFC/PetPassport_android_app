package com.example.petpassport_android_app.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.data.dto.User.PhotoDto
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val apiService: PetApiService,
    private val prefs: SharedPreferences
) : PetRepository {

    private fun getOwnerId(): Int = prefs.getInt("owner_id", 0)

    override suspend fun getPetById(id: Int): Pet? {
        return try {
            apiService.getPet(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPet(pet: Pet): Pet? {
        return try {
            val dto = pet.toDto(getOwnerId())
            val created = apiService.createPet(dto).toDomain()
            Log.d("PetRepository", "Pet создан: $created")
            created
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updatePet(id: Int, pet: Pet): Pet? {
        return try {
            val dto = pet.toDto(getOwnerId())
            apiService.updatePet(id, dto).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun uploadPetPhoto(petId: Int, imageBytes: ByteArray): Result<PhotoDto> {
        val part = MultipartBody.Part.createFormData(
            name = "newFiles",
            filename = "pet_photo.jpg",
            body = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        return try {
            val response = apiService.uploadPetPhoto(
                petId = petId,
                newFiles = listOf(part)
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Ошибка ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}