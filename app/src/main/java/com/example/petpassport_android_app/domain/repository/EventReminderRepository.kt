package com.example.petpassport_android_app.domain.repository

data class EventReminderState(
    val enabled: Boolean,
    val offsetsMinutes: List<Long>,
    val titleForNotification: String,
    val eventDateIso: String,
)

interface EventReminderRepository {
    suspend fun setState(
        eventId: Int,
        enabled: Boolean,
        offsetsMinutes: List<Long>,
        titleForNotification: String,
        eventDateIso: String,
    )

    suspend fun removeState(eventId: Int)

    suspend fun snapshot(): Map<Int, EventReminderState>
}
