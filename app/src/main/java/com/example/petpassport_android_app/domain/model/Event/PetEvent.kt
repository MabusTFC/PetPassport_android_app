package com.example.petpassport_android_app.domain.model.Event
import android.os.Parcelable

sealed interface PetEvent : Parcelable {
    val id: Int
    val title: String
    val date: String
    val petId: Int
    val reminderEnabled: Boolean
    val reminderOffsetsMinutes: List<Long>
}