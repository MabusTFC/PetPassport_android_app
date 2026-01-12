package com.example.petpassport_android_app.domain.model

data class Owner(
    val id: Int? = null,
    val login: String,
    val telegramId: String? = null,
    val telegramNick: String? = null,
    val pets: List<Pet> = emptyList()
)