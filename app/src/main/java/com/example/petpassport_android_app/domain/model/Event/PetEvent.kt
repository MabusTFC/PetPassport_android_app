package com.example.petpassport_android_app.domain.model.Event

sealed interface PetEvent {
    val id: Int
    val title: String
    val date: String
    val petId: Int
}