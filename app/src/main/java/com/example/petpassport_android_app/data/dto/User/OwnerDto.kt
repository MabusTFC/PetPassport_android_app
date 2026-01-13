package com.example.petpassport_android_app.data.dto.User

import com.google.gson.annotations.SerializedName

data class OwnerDto(
    @SerializedName("ownerId") val ownerId: Int?,
    @SerializedName("login") val login: String,
    @SerializedName("telegramId") val telegramId: String?,
    @SerializedName("telegramNick") val telegramNick: String?,
    @SerializedName("pets") val pets: List<OwnerPetDto>?
)

data class OwnerPetDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class OwnerLoginRegisterDto(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
)

data class OwnerLoginResultDto(
    @SerializedName("ownerId") val ownerId: Int
)

data class PetSummaryDto(
    val id: Int,
    val name: String
)


