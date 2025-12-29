package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.data.mapper.toPet
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val apiService: PetApiService
) : PetRepository {

    override suspend fun getPetById(id: Int): Pet? {
        return try {
            apiService.getPet(id).toPet()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPet(pet: Pet): Pet? {
        return try {
            val dto = pet.toDto()
            apiService.createPet(dto).toPet()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updatePet(id: Int, pet: Pet): Pet? {
        return try {
            val dto = pet.toDto()
            apiService.updatePet(id, dto).toPet()
        } catch (e: Exception) {
            null
        }
    }
}