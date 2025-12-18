package com.example.petpassport_android_app.data.dto

import com.google.gson.annotations.SerializedName

data class DoctorVisitDto(
    @SerializedName("title") val title: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("petId") val petId: Int,
    @SerializedName("clinic") val clinic: String?,
    @SerializedName("doctor") val doctor: String?,
    @SerializedName("diagnosis") val diagnosis: String?,
    @SerializedName("recommendations") val recommendations: String?
)
