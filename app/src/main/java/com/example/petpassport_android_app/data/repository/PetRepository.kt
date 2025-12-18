package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.domain.model.Pet
import com.example.petpassport_android_app.domain.repository.PetRepository
import javax.inject.Inject
import javax.inject.Singleton

class PetRepositoryImpl @Inject constructor(
    private val apiService: PetApiService
) : PetRepository { // Реализуем интерфейс
    override suspend fun getAllPets(): List<Pet> {
        TODO("Not yet implemented")
    }

    override suspend fun getPetById(id: Int): Pet? {
        TODO("Not yet implemented")
    }

    override suspend fun deletePet(id: Int): Boolean {
        TODO("Not yet implemented")
    }


}