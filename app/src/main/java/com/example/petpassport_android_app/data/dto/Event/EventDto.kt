package com.example.petpassport_android_app.data.dto.Event

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("clinic") val clinic: String?,
    @SerializedName("doctor") val doctor: String?,
    @SerializedName("diagnosis") val diagnosis: String?,
    @SerializedName("medicine") val medicine: String?,
    @SerializedName("remedy") val remedy: String?,
    @SerializedName("parasite") val parasite: String?,
    @SerializedName("nextTreatmentDate") val nextTreatmentDate: String?,
    @SerializedName("reminderEnabled") val reminderEnabled: Boolean? = null,
    @SerializedName("status") val status: Int? = null,
)