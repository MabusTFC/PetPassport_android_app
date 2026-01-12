package com.example.petpassport_android_app.data.dto.Event

import com.google.gson.annotations.SerializedName

data class TreatmentDto(
    @SerializedName("title") val title: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("petId") val petId: Int,
    @SerializedName("remedy") val remedy: String?,
    @SerializedName("parasite") val parasite: String?,
    @SerializedName("nextTreatmentDate") val nextTreatmentDate: String?
)