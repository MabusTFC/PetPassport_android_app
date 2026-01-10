package com.example.petpassport_android_app.domain.model

data class Owner(
    val id: Int,
    val telegramId: String,
    val telegramNick: String,
    val pets: List<Pet> = emptyList()
)