package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import javax.inject.Inject

class EventMedicineRepositoryImpl @Inject constructor(
    private val apiService: PetApiService
) : EventMedicineRepository {
    override suspend fun getAllEvents(): List<PetEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun getEventById(id: Int): PetEvent? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}