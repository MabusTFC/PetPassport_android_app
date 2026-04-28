package com.example.petpassport_android_app.domain.model.Event

import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaccine(
    override val id: Int,
    override val title: String,
    override val date: String,
    override val petId: Int,
    val medicine: String,
    override val reminderEnabled: Boolean = true,
    override val reminderOffsetsMinutes: List<Long> = emptyList()
): PetEvent