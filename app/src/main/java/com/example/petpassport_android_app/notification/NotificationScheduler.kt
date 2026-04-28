package com.example.petpassport_android_app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.ZonedDateTime

object NotificationScheduler {
    val legacyDefaultOffsetsMinutes: List<Long> = listOf(5L, 60L, 1440L)

    fun schedule(
        context: Context,
        eventId: Int,
        eventTitle: String,
        eventDate: ZonedDateTime,
        minutesBefore: Long
    ) {
        val triggerTime = eventDate.minusMinutes(minutesBefore).toInstant().toEpochMilli()
        if (triggerTime <= System.currentTimeMillis()) return // уже прошло

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", "Напоминание: $eventTitle")
            putExtra("message", "Процедура через ${formatMinutes(minutesBefore)}")
            putExtra("eventId", eventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId * 1000 + minutesBefore.toInt(), // уникальный id для каждого напоминания
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val showLaunch = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val showPending = showLaunch?.let { launchIntent ->
            PendingIntent.getActivity(
                context,
                (eventId * 31 + minutesBefore.toInt()) and 0x7FFF_FFFF,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } catch (_: SecurityException) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else if (showPending != null) {
                try {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(triggerTime, showPending),
                        pendingIntent,
                    )
                } catch (_: SecurityException) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } else {
            @Suppress("DEPRECATION")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    fun cancel(context: Context, eventId: Int, minutesBefore: Long) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId * 1000 + minutesBefore.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun cancelAll(context: Context, eventId: Int, offsetsMinutes: Collection<Long> = legacyDefaultOffsetsMinutes) {
        offsetsMinutes.distinct().forEach { cancel(context, eventId, it) }
    }

    private fun formatMinutes(minutes: Long): String = when {
        minutes < 60 -> "$minutes минут"
        minutes < 1440 -> "${minutes / 60} час(а)"
        else -> "${minutes / 1440} день(дней)"
    }
}