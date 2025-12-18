package com.example.petpassport_android_app.domain.model

data class Owner(
    val id: Int,
    val telegramNick: String,
    val petCount: Int,
    val summary: String
)