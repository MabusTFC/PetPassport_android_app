package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.*
import com.example.petpassport_android_app.domain.model.Event.*


fun VaccineDto.toDomain(): Vaccine {
    return Vaccine(
        id = 0,
        title = this.title,
        date = this.eventDate.substringBefore("T"),
        petId = this.petId,
        medicine = this.medicine ?: "Не указано"
    )
}

fun Vaccine.toDto(): VaccineDto {
    return VaccineDto(
        title = this.title,
        eventDate = this.date,
        reminderEnabled = false,
        petId = this.petId,
        medicine = this.medicine,
        nextVaccinationDate = null
    )
}

fun TreatmentDto.toDomain(): Treatment {
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

fun Treatment.toDto(): TreatmentDto {
    return TreatmentDto(
        title = this.title,
        eventDate = this.date,
        petId = this.petId,
        remedy = this.remedy,
        parasite = this.parasite,
        nextTreatmentDate = this.nextTreatmentDate
    )
}


fun DoctorVisitDto.toDomain(): DoctorVisit {
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

fun DoctorVisit.toDto(): DoctorVisitDto {
    return DoctorVisitDto(
        title = this.title,
        eventDate = this.date,
        petId = this.petId,
        clinic = this.clinic,
        doctor = this.doctor,
        diagnosis = this.diagnosis,
        recommendations = null
    )
}

fun EventDto.toDomain(): PetEvent {
    return when(this.type) {
        "Vaccine" -> {
            Vaccine(
                id = this.id,
                title = this.title,
                date = this.eventDate.substringBefore("T"),
                petId = 0,
                medicine = this.medicine ?: "Не указано"
            )
        }

        "Treatment" -> {
            Treatment(
                id = this.id,
                title = this.title,
                date = this.eventDate.substringBefore("T"),
                petId = 0,
                remedy = this.remedy ?: "Не указано",
                parasite = "Не указано",
                nextTreatmentDate = ""
            )
        }

        "DoctorVisit" -> {
            DoctorVisit(
                id = this.id,
                title = this.title,
                date = this.eventDate.substringBefore("T"),
                petId = 0,
                clinic = this.clinic ?: "Не указана",
                doctor = "Не указан",
                diagnosis = "Нет диагноза"
            )
        }
        else -> throw IllegalArgumentException()
    }
}