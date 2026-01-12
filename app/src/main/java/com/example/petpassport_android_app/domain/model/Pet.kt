package com.example.petpassport_android_app.domain.model

data class Pet(
    val id: Int,
    val name: String,
    val breed: String,
    val weight: Double,
    val birthDate: String,
    val photoUrl: String?
)