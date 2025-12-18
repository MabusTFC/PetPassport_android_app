package com.example.petpassport_android_app.domain.model.Event

data class DoctorVisit(
    override val id: Int,
    override val title: String,
    override val date: String,
    override val petId: Int,
    val clinic: String,
    val doctor: String,
    val diagnosis: String
): PetEvent