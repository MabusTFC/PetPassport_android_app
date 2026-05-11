package com.example.petpassport_android_app.notification

import android.content.Context
import com.example.petpassport_android_app.domain.model.Event.PetEvent
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object EventNotificationPlanner {
    const val defaultOffsetIfEmptyMinutes: Long = 60L

    fun localEventDateTimeIso(date: String, time: String): String {
        val localDateTime = LocalDateTime.parse("${date}T${time}:00")
        return localDateTime
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    fun offsetsToCancelBeforeReschedule(
        previouslyStoredOffsets: Collection<Long>,
        modelOffsets: Collection<Long>,
    ): List<Long> {
        return (NotificationScheduler.legacyDefaultOffsetsMinutes +
            previouslyStoredOffsets +
            modelOffsets +
            defaultOffsetIfEmptyMinutes)
            .distinct()
    }

    fun reschedule(
        context: Context,
        event: PetEvent,
        globalNotificationsEnabled: Boolean,
        cancelOffsets: Collection<Long>,
    ) {
        NotificationScheduler.cancelAll(context, event.id, cancelOffsets)

        if (!globalNotificationsEnabled || !event.reminderEnabled) return
        val eventDate = parseEventDate(event.date) ?: return
        effectiveOffsets(event.reminderOffsetsMinutes).forEach { minutes ->
            NotificationScheduler.schedule(
                context = context,
                eventId = event.id,
                eventTitle = event.title,
                eventDate = eventDate,
                minutesBefore = minutes,
            )
        }
    }

    private fun effectiveOffsets(offsetsMinutes: Collection<Long>): List<Long> {
        return offsetsMinutes
            .ifEmpty { listOf(defaultOffsetIfEmptyMinutes) }
            .filter { it >= 0L }
            .distinct()
            .sorted()
    }

    private fun parseEventDate(date: String): ZonedDateTime? {
        return runCatching { ZonedDateTime.parse(date) }.getOrNull()
            ?: runCatching { OffsetDateTime.parse(date).toZonedDateTime() }.getOrNull()
            ?: runCatching {
                LocalDateTime.parse(date.substringBefore("Z")).atZone(ZoneId.systemDefault())
            }.getOrNull()
    }
}

suspend fun syncPerEventReminderFromBell(
    context: Context,
    event: PetEvent,
    enabled: Boolean,
    eventReminderRepository: EventReminderRepository,
    notificationPreferencesRepository: NotificationPreferencesRepository,
) {
    val store = eventReminderRepository.snapshot()
    val previous = store[event.id]
    val offsets = when {
        !enabled -> emptyList()
        event.reminderOffsetsMinutes.isNotEmpty() -> event.reminderOffsetsMinutes
        previous?.offsetsMinutes?.isNotEmpty() == true -> previous.offsetsMinutes
        else -> listOf(EventNotificationPlanner.defaultOffsetIfEmptyMinutes)
    }
    eventReminderRepository.setState(
        eventId = event.id,
        enabled = enabled,
        offsetsMinutes = offsets,
        titleForNotification = event.title,
        eventDateIso = event.date,
    )

    val updated = event.withReminderState(enabled, offsets)
    EventNotificationPlanner.reschedule(
        context = context,
        event = updated,
        globalNotificationsEnabled = notificationPreferencesRepository.isNotificationsEnabled.first(),
        cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
            previouslyStoredOffsets = previous?.offsetsMinutes.orEmpty(),
            modelOffsets = event.reminderOffsetsMinutes,
        ),
    )
}

fun rescheduleFromModel(
    context: Context,
    event: PetEvent,
    globalNotificationsEnabled: Boolean,
    previouslyStoredOffsets: Collection<Long> = emptyList(),
) {
    EventNotificationPlanner.reschedule(
        context = context,
        event = event,
        globalNotificationsEnabled = globalNotificationsEnabled,
        cancelOffsets = EventNotificationPlanner.offsetsToCancelBeforeReschedule(
            previouslyStoredOffsets = previouslyStoredOffsets,
            modelOffsets = event.reminderOffsetsMinutes,
        ),
    )
}

private fun PetEvent.withReminderState(enabled: Boolean, offsetsMinutes: List<Long>): PetEvent {
    return when (this) {
        is com.example.petpassport_android_app.domain.model.Event.Vaccine -> copy(
            reminderEnabled = enabled,
            reminderOffsetsMinutes = offsetsMinutes,
        )
        is com.example.petpassport_android_app.domain.model.Event.Treatment -> copy(
            reminderEnabled = enabled,
            reminderOffsetsMinutes = offsetsMinutes,
        )
        is com.example.petpassport_android_app.domain.model.Event.DoctorVisit -> copy(
            reminderEnabled = enabled,
            reminderOffsetsMinutes = offsetsMinutes,
        )
    }
}
