package com.example.petpassport_android_app.data.dto.User

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("ownerId") val ownerId: Int
)

data class RefreshRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)

data class LogoutRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)