package com.example.petpassport_android_app.data.dto.Event

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("clinic") val clinic: String?,
    @SerializedName("medicine") val medicine: String?,
    @SerializedName("remedy") val remedy: String?
)