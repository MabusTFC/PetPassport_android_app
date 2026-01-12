package com.example.petpassport_android_app.data.dto.User

import com.google.gson.annotations.SerializedName

data class PetDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("breed") val breed: String?,
    @SerializedName("weightKg") val weight: Double,
    @SerializedName("birthDate") val birthDate: String,
    @SerializedName("ownerId") val ownerId: Int,
    @SerializedName("photos") val photo: List<PhotoDto>?
)
data class PhotoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String?,
    @SerializedName("telegramFileId") val telegramFileId: String?
)