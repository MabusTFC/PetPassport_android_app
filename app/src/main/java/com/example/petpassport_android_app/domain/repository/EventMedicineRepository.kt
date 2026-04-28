package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine

interface EventMedicineRepository {
    suspend fun getPetEvents(petId: Int): List<PetEvent>

    suspend fun addVaccine(vaccine: Vaccine): Int?
    suspend fun addTreatment(treatment: Treatment): Int?
    suspend fun addDoctorVisit(visit: DoctorVisit): Int?

    suspend fun deleteVaccine(id: Int): Boolean
    suspend fun deleteTreatment(id: Int): Boolean
    suspend fun deleteDoctorVisit(id: Int): Boolean

    suspend fun updateVaccine(id: Int, vaccine: Vaccine): Boolean
    suspend fun updateTreatment(id: Int, treatment: Treatment): Boolean
    suspend fun updateDoctorVisit(id: Int, visit: DoctorVisit): Boolean

}