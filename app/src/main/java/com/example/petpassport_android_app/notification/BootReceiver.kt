package com.example.petpassport_android_app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.petpassport_android_app.di.ReminderBootEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val pendingResult = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val entry = EntryPointAccessors.fromApplication(
                    appContext,
                    ReminderBootEntryPoint::class.java,
                )
                val global = entry.notificationPreferencesRepository().isNotificationsEnabled.first()
                if (!global) return@launch
                val snapshot = entry.eventReminderRepository().snapshot()
                for ((eventId, st) in snapshot) {
                    if (!st.enabled || st.offsetsMinutes.isEmpty()) continue
                    if (st.eventDateIso.isBlank()) continue
                    val title = st.titleForNotification.ifBlank { "Процедура" }
                    val zoned = try {
                        ZonedDateTime.parse(st.eventDateIso)
                    } catch (_: Exception) {
                        continue
                    }
                    st.offsetsMinutes.distinct().forEach { minutes ->
                        NotificationScheduler.schedule(
                            context = appContext,
                            eventId = eventId,
                            eventTitle = title,
                            eventDate = zoned,
                            minutesBefore = minutes,
                        )
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
