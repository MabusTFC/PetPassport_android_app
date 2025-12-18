package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Pet

interface PetRepository {
    suspend fun getAllPets(): List<Pet>
    suspend fun getPetById(id: Int): Pet?
    suspend fun deletePet(id: Int): Boolean
}