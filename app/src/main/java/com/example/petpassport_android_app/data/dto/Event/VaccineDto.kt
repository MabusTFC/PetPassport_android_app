package com.example.petpassport_android_app.data.dto.Event

import com.google.gson.annotations.SerializedName

data class VaccineDto(
    @SerializedName("title") val title: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("reminderEnabled") val reminderEnabled: Boolean,
    @SerializedName("petId") val petId: Int,
    @SerializedName("medicine") val medicine: String?,
    @SerializedName("nextVaccinationDate") val nextVaccinationDate: String?
)