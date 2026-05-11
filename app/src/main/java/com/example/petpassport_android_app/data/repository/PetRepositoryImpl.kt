package com.example.petpassport_android_app.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.data.dto.User.PhotoDto
import com.example.petpassport_android_app.data.dto.User.PetUpdateDto
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val apiService: PetApiService,
    private val prefs: SharedPreferences,
    @param:ApplicationContext private val context: Context,
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
            val createdId = apiService.createPet(
                name = pet.name.toPlainTextRequestBody(),
                breed = pet.breed.toPlainTextRequestBody(),
                weightKg = pet.weight.toString().toPlainTextRequestBody(),
                birthDate = pet.birthDate.substringBefore("T").toPlainTextRequestBody(),
            )
            pet.photoUrl.toPhotoPart(partName = "file")?.let { photoPart ->
                apiService.uploadPetPhoto(createdId, photoPart)
            }
            val created = apiService.getPet(createdId).toDomain()
            Log.d("PetRepository", "Pet создан: $created")
            created
        } catch (e: HttpException) {
            Log.e("PetRepository", "Ошибка создания питомца: HTTP ${e.code()} ${e.response()?.errorBody()?.string()}")
            null
        } catch (e: Exception) {
            Log.e("PetRepository", "Ошибка создания питомца", e)
            null
        }
    }

    override suspend fun updatePet(id: Int, pet: Pet): Pet? {
        return try {
            val dto = PetUpdateDto(
                name = pet.name,
                breed = pet.breed,
                weight = pet.weight,
                birthDate = pet.birthDate.substringBefore("T"),
            )
            val response = apiService.updatePet(id, dto)
            if (!response.isSuccessful) return null
            apiService.getPet(id).toDomain()
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
            val currentPet = apiService.getPet(petId)
            val deletePhotoIds = currentPet.photo.orEmpty().map { it.id }
            val response = apiService.replacePetPhotos(
                petId = petId,
                newFiles = listOf(part),
                deletePhotoIds = deletePhotoIds,
            )

            if (response.isSuccessful) {
                val updatedPhoto = apiService.getPet(petId).photo?.firstOrNull()
                if (updatedPhoto != null) {
                    Result.success(updatedPhoto)
                } else {
                    Result.failure(Exception("Фото не найдено после загрузки"))
                }
            } else {
                Result.failure(Exception("Ошибка ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePetPhoto(petId: Int, photoId: Int): Result<Unit> {
        return try {
            val response = apiService.deletePetPhoto(petId = petId, photoId = photoId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Ошибка ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun String?.toPhotoPart(partName: String): MultipartBody.Part? {
        if (isNullOrBlank()) return null

        return try {
            val uri = Uri.parse(this)
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return null
            val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(
                name = partName,
                filename = "pet_photo.${mimeType.substringAfter('/', "jpeg")}",
                body = body,
            )
        } catch (e: Exception) {
            Log.e("PetRepository", "Не удалось прочитать фото питомца", e)
            null
        }
    }
}

private fun String.toPlainTextRequestBody() =
    toRequestBody("text/plain".toMediaTypeOrNull())
