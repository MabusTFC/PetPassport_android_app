package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.DoctorVisitDto
import com.example.petpassport_android_app.data.dto.TreatmentDto
import com.example.petpassport_android_app.data.dto.VaccineDto
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine

fun VaccineDto.toVaccine(): Vaccine{
    return Vaccine(
        id = 0,
        title = this.title,
        date = this.eventDate.substringBefore("T"),
        petId = this.petId,
        medicine = this.medicine ?: "Не указано"
    )
}

fun TreatmentDto.toTreatmentDto(): Treatment{
    return Treatment(
        id = 0,
        title = this.title,
        date = this.eventDate.substringBefore("T"),
        petId = this.petId,
        remedy = this.remedy ?: "Не указано",
        parasite = this.parasite ?: "Не указано",
        nextTreatmentDate = this.nextTreatmentDate?.substringBefore("T") ?: ""
    )
}

fun DoctorVisitDto.toDoctorVisit(): DoctorVisit{
    return DoctorVisit(
        id = 0,
        title = this.title,
        date = this.eventDate.substringBefore("T"),
        petId = this.petId,
        clinic = this.clinic ?: "Не указана",
        doctor = this.doctor ?: "Не указан",
        diagnosis = this.diagnosis ?: "Нет диагноза"
    )
}