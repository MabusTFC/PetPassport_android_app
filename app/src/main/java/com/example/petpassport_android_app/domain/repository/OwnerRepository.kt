package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.model.Pet

interface OwnerRepository {
    suspend fun registerOwner(owner: Owner): Owner?
    suspend fun getPetsByOwner(telegramId: Long): List<Pet>
}