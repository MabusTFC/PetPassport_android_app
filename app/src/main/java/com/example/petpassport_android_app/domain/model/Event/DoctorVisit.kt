package com.example.petpassport_android_app.domain.model.Event

import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorVisit(
    override val id: Int,
    override val title: String,
    override val date: String,
    override val petId: Int,
    val clinic: String,
    val doctor: String,
    val diagnosis: String,
    override val reminderEnabled: Boolean = true,
    override val reminderOffsetsMinutes: List<Long> = emptyList()
): PetEvent