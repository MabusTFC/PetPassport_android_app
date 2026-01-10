package com.example.petpassport_android_app.data.dto

import com.google.gson.annotations.SerializedName

data class OwnerDto(
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("telegramId") val telegramId: String,
    @SerializedName("telegramNick") val telegramNick: String,
    @SerializedName("pets") val pets: List<OwnerPetDto>?
)

data class OwnerPetDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)