package com.example.petpassport_android_app.data.repository

import android.content.Context
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.EventReminderState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventReminderRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : EventReminderRepository {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override suspend fun setState(
        eventId: Int,
        enabled: Boolean,
        offsetsMinutes: List<Long>,
        titleForNotification: String,
        eventDateIso: String,
    ) {
        val normalizedOffsets = offsetsMinutes
            .filter { it >= 0L }
            .distinct()
            .sorted()
        preferences.edit()
            .putString(
                keyFor(eventId),
                listOf(
                    enabled.toString(),
                    normalizedOffsets.joinToString(","),
                    titleForNotification.encodeToStorage(),
                    eventDateIso.encodeToStorage(),
                ).joinToString(SEPARATOR),
            )
            .apply()
    }

    override suspend fun removeState(eventId: Int) {
        preferences.edit().remove(keyFor(eventId)).apply()
    }

    override suspend fun snapshot(): Map<Int, EventReminderState> {
        return preferences.all.mapNotNull { (key, value) ->
            if (!key.startsWith(KEY_PREFIX)) return@mapNotNull null
            val eventId = key.removePrefix(KEY_PREFIX).toIntOrNull() ?: return@mapNotNull null
            val state = (value as? String)?.toState() ?: return@mapNotNull null
            eventId to state
        }.toMap()
    }

    private fun String.toState(): EventReminderState? {
        val parts = split(SEPARATOR)
        if (parts.size < 4) return null
        val offsets = parts[1]
            .split(",")
            .mapNotNull { it.toLongOrNull() }
            .distinct()
            .sorted()
        return EventReminderState(
            enabled = parts[0].toBooleanStrictOrNull() ?: false,
            offsetsMinutes = offsets,
            titleForNotification = parts[2].decodeFromStorage(),
            eventDateIso = parts[3].decodeFromStorage(),
        )
    }

    private fun keyFor(eventId: Int): String = "$KEY_PREFIX$eventId"

    private fun String.encodeToStorage(): String =
        replace("%", "%25").replace(SEPARATOR, "%7C")

    private fun String.decodeFromStorage(): String =
        replace("%7C", SEPARATOR).replace("%25", "%")

    private companion object {
        const val PREFERENCES_NAME = "event_reminders"
        const val KEY_PREFIX = "event_"
        const val SEPARATOR = "|"
    }
}
