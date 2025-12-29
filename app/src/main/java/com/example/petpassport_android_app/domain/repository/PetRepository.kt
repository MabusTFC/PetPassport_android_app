package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Pet

interface PetRepository {
    suspend fun getPetById(id: Int): Pet?
    suspend fun createPet(pet: Pet): Pet?
    suspend fun updatePet(id: Int, pet: Pet): Pet?
}