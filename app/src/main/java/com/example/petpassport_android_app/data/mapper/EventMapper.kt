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
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun VaccineDto.toDomain(): Vaccine {
    return Vaccine(
        id = this.id ?: 0,
        title = this.title,
        date = this.eventDate.toLocalEventIso(),
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
        eventDate = this.date.toUtcApiIso(),
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
        date = this.eventDate.toLocalEventIso(),
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
        eventDate = this.date.toUtcApiIso(),
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
        date = this.eventDate.toLocalEventIso(),
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
        eventDate = this.date.toUtcApiIso(),
        petId = this.petId,
        clinic = this.clinic,
        doctor = this.doctor,
        diagnosis = this.diagnosis,
        recommendations = null,
    )
}

fun EventDto.toDomain(): PetEvent {
    return when (this.type) {
        "vaccine" -> Vaccine(
            id = this.id,
            title = this.title,
            date = this.eventDate.toLocalEventIso(),
            petId = 0,
            medicine = this.medicine ?: "Не указано",
            reminderEnabled = this.reminderEnabled ?: false,
            reminderOffsetsMinutes = emptyList(),
        )
        "treatment" -> Treatment(
            id = this.id,
            title = this.title,
            date = this.eventDate.toLocalEventIso(),
            petId = 0,
            remedy = this.remedy ?: "Не указано",
            parasite = this.parasite ?: "Не указано",
            nextTreatmentDate = this.nextTreatmentDate?.substringBefore("T"),
            reminderEnabled = this.reminderEnabled ?: false,
            reminderOffsetsMinutes = emptyList(),
        )
        "doctor-visit" -> DoctorVisit(
            id = this.id,
            title = this.title,
            date = this.eventDate.toLocalEventIso(),
            petId = 0,
            clinic = this.clinic ?: "Не указана",
            doctor = this.doctor ?: "Не указан",
            diagnosis = this.diagnosis ?: "Нет диагноза",
            reminderEnabled = this.reminderEnabled ?: false,
            reminderOffsetsMinutes = emptyList(),
        )
        else -> throw IllegalArgumentException("Unknown type: ${this.type}")
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

private fun String.toUtcApiIso(): String {
    val zoned = runCatching { ZonedDateTime.parse(this) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(this).toZonedDateTime() }.getOrNull()
        ?: runCatching {
            LocalDateTime.parse(substringBefore("Z")).atZone(ZoneId.systemDefault())
        }.getOrNull()
        ?: return this

    return zoned
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT)
}

private fun String.toLocalEventIso(): String {
    val zoned = runCatching { ZonedDateTime.parse(this) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(this).toZonedDateTime() }.getOrNull()
        ?: runCatching {
            LocalDateTime.parse(substringBefore("Z")).atZone(ZoneOffset.UTC)
        }.getOrNull()
        ?: return this

    return zoned
        .withZoneSameInstant(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
