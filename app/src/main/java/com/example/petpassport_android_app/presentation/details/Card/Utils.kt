package com.example.petpassport_android_app.presentation.details.Card

import androidx.annotation.DrawableRes
import com.example.petpassport_android_app.R
import com.example.petpassport_android_app.domain.model.Event.DoctorVisit
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.model.Event.Treatment
import com.example.petpassport_android_app.domain.model.Event.Vaccine
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@DrawableRes
fun eventIconRes(event: PetEvent): Int = when (event) {
    is Vaccine -> R.drawable.medical_folder
    is Treatment -> R.drawable.medical_folder
    is DoctorVisit -> R.drawable.medical_folder
}

fun formatEventDate(iso: String): String {
    return try {
        Instant.parse(iso)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: Exception) {
        iso
    }
}

/** Человекочитаемый формат для UI */
fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

/** ISO 8601 формат для базы */
fun formatDateForDatabase(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.of("UTC"))
        .format(DateTimeFormatter.ISO_INSTANT)
}