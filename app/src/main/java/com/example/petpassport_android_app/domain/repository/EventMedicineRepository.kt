package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine

interface EventMedicineRepository {
    suspend fun getPetEvents(petId: Int): List<PetEvent>
    suspend fun addVaccine(vaccine: Vaccine): Boolean
    suspend fun addTreatment(treatment: Treatment): Boolean
    suspend fun addDoctorVisit(visit: DoctorVisit): Boolean

}