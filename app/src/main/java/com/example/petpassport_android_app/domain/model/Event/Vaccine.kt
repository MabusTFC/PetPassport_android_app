package com.example.petpassport_android_app.domain.model.Event

import com.example.petpassport_android_app.domain.model.Event.PetEvent

data class Vaccine(
    override val id: Int,
    override val title: String,
    override val date: String,
    override val petId: Int,
    val medicine: String
): PetEvent