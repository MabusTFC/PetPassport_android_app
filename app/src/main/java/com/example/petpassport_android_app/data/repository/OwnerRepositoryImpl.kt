package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import javax.inject.Inject

class OwnerRepositoryImpl @Inject constructor(
    private val apiService: OwnerApiService
) : OwnerRepository {
    override suspend fun registerOwner(owner: Owner): Owner? {
        TODO("Not yet implemented")
    }

    override suspend fun getPetsByOwner(telegramId: Long): List<Pet> {
        TODO("Not yet implemented")
    }
}