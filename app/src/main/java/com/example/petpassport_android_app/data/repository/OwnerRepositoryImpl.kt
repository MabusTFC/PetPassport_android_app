package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import javax.inject.Inject

class OwnerRepositoryImpl @Inject constructor(
    private val api: OwnerApiService
) : OwnerRepository {

    override suspend fun getPetsByOwner(ownerId: Int): List<Pet> {
        return try {
            api.getPets(ownerId).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}