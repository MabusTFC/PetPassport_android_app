package com.example.petpassport_android_app.data.dto

data class AuthStatusDto(
    val status: String,
    val telegramId: String? = null,
    val accessToken: String? = null
)
