package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import javax.inject.Inject

class OwnerRepositoryImpl @Inject constructor(
    private val apiService: OwnerApiService
) : OwnerRepository {
    override suspend fun registerOwner(owner: Owner): Owner? {
        return try{
            apiService.registerOwner(owner.toDto()).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPetsByOwner(telegramId: String): List<Pet> {
        return try {
            apiService.getPetsByOwner(telegramId).map{
                it.toDomain()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getOwnerByTelegramId(telegramId: String): Owner? {
        return try {
            apiService.getOwnerByTelegramId(telegramId).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}