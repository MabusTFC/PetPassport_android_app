package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet

interface OwnerRepository {
    suspend fun getPetsByOwner(ownerId: Int): List<Pet>
}
