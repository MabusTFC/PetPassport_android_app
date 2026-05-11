package com.example.petpassport_android_app.domain.model.Event

data class EventReminderUiPayload(
    val enabled: Boolean,
    val offsetsMinutes: List<Long>,
)
