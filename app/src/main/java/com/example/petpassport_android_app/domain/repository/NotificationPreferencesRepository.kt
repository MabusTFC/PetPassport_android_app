package com.example.petpassport_android_app.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationPreferencesRepository {
    val isNotificationsEnabled: Flow<Boolean>

    suspend fun setNotificationsEnabled(enabled: Boolean)
}