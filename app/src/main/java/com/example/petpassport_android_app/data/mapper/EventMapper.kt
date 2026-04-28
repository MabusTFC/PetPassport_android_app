package com.example.petpassport_android_app.data.mapper

import com.example.petpassport_android_app.data.dto.Event.DoctorVisitDto
import com.example.petpassport_android_app.data.dto.Event.EventDto
import com.example.petpassport_android_app.data.dto.Event.TreatmentDto
import com.example.petpassport_android_app.data.dto.Event.VaccineDto
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import com.example.petpassport_android_app.domain.repository.EventReminderState

fun VaccineDto.toDomain(): Vaccine {
    return Vaccine(
        id = this.id ?: 0,
        title = this.title,
        date = this.eventDate,
        petId = this.petId,
        medicine = this.medicine ?: "Не указано",
        reminderEnabled = this.reminderEnabled,
        reminderOffsetsMinutes = emptyList(),
    )
}

fun Vaccine.toDto(): VaccineDto {
    return VaccineDto(
        id = id.takeIf { it > 0 },
        title = this.title,
        eventDate = this.date,
        reminderEnabled = this.reminderEnabled,
        petId = this.petId,
        medicine = this.medicine,
        nextVaccinationDate = null,
    )
}

fun TreatmentDto.toDomain(): Treatment {
    return Treatment(
        id = id ?: 0,
        title = this.title,
        date = this.eventDate,
        petId = this.petId,
        remedy = this.remedy ?: "Не указано",
        parasite = this.parasite ?: "Не указано",
        nextTreatmentDate = this.nextTreatmentDate?.substringBefore("T"),
    )
}

fun Treatment.toDto(): TreatmentDto {
    return TreatmentDto(
        id = id.takeIf { it > 0 },
        title = this.title,
        eventDate = this.date,
        petId = this.petId,
        remedy = this.remedy,
        parasite = this.parasite,
        nextTreatmentDate = this.nextTreatmentDate,
    )
}

fun DoctorVisitDto.toDomain(): DoctorVisit {
    return DoctorVisit(
        id = id ?: 0,
        title = this.title,
        date = this.eventDate,
        petId = this.petId,
        clinic = this.clinic ?: "Не указана",
        doctor = this.doctor ?: "Не указан",
        diagnosis = this.diagnosis ?: "Нет диагноза",
    )
}

fun DoctorVisit.toDto(): DoctorVisitDto {
    return DoctorVisitDto(
        id = id.takeIf { it > 0 },
        title = this.title,
        eventDate = this.date,
        petId = this.petId,
        clinic = this.clinic,
        doctor = this.doctor,
        diagnosis = this.diagnosis,
        recommendations = null,
    )
}

fun EventDto.toDomain(): PetEvent {
    return when (this.type) {
        "vaccine" -> {
            Vaccine(
                id = this.id,
                title = this.title,
                date = this.eventDate,
                petId = 0,
                medicine = this.medicine ?: "Не указано",
                reminderEnabled = this.reminderEnabled ?: true,
                reminderOffsetsMinutes = emptyList(),
            )
        }

        "treatment" -> {
            Treatment(
                id = this.id,
                title = this.title,
                date = this.eventDate,
                petId = 0,
                remedy = this.remedy ?: "Не указано",
                parasite = "Не указано",
                nextTreatmentDate = null,
            )
        }

        "doctor-visit" -> {
            DoctorVisit(
                id = this.id,
                title = this.title,
                date = this.eventDate,
                petId = 0,
                clinic = this.clinic ?: "Не указана",
                doctor = "Не указан",
                diagnosis = "Нет диагноза",
            )
        }

        else -> throw IllegalArgumentException()
    }
}

fun PetEvent.mergeReminderFromStore(state: EventReminderState): PetEvent = when (this) {
    is Vaccine -> copy(
        reminderEnabled = state.enabled,
        reminderOffsetsMinutes = state.offsetsMinutes,
    )
    is Treatment -> copy(
        reminderEnabled = state.enabled,
        reminderOffsetsMinutes = state.offsetsMinutes,
    )
    is DoctorVisit -> copy(
        reminderEnabled = state.enabled,
        reminderOffsetsMinutes = state.offsetsMinutes,
    )
}

fun List<PetEvent>.mergeReminderStore(store: Map<Int, EventReminderState>): List<PetEvent> =
    map { event -> store[event.id]?.let { event.mergeReminderFromStore(it) } ?: event }
