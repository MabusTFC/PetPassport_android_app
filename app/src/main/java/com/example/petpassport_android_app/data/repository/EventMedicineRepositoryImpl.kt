package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import javax.inject.Inject

class EventMedicineRepositoryImpl @Inject constructor(
    private val apiService: PetApiService
) : EventMedicineRepository {
    override suspend fun getPetEvents(petId: Int): List<PetEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun addVaccine(vaccine: Vaccine): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addTreatment(treatment: Treatment): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addDoctorVisit(visit: DoctorVisit): Boolean {
        TODO("Not yet implemented")
    }
}