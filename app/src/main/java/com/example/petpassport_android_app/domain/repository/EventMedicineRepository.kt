package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Event.PetEvent

interface EventMedicineRepository {
    suspend fun getAllEvents(): List<PetEvent>
    suspend fun getEventById(id: Int): PetEvent?
    suspend fun deleteEvent(id: Int): Boolean

}