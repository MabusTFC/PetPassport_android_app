package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.DoctorVisitApiService
import com.example.petpassport_android_app.data.api.EventsApiService
import com.example.petpassport_android_app.data.api.TreatmentApiService
import com.example.petpassport_android_app.data.api.VaccineApiService
import com.example.petpassport_android_app.data.mapper.toDto
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import javax.inject.Inject

class EventMedicineRepositoryImpl @Inject constructor(
    private val eventsApi: EventsApiService,
    private val vaccineApi: VaccineApiService,
    private val treatmentApi: TreatmentApiService,
    private val doctorApi: DoctorVisitApiService
) : EventMedicineRepository {
    override suspend fun getPetEvents(petId: Int): List<PetEvent> {
        return try {
            eventsApi.getUpcomingEvents(petId).map {
                it.toDomain()
            }
            } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addVaccine(vaccine: Vaccine): Boolean {
        return try {
            vaccineApi.createVaccine(vaccine.toDto())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addTreatment(treatment: Treatment): Boolean {
        return try {
            treatmentApi.createTreatment(treatment.toDto())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addDoctorVisit(visit: DoctorVisit): Boolean {
        return try {
            doctorApi.createDoctorVisit(visit.toDto())
            true
        } catch (e: Exception) {
            false
        }
    }
}