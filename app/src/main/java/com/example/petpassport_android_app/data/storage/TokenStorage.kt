package com.example.petpassport_android_app.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "auth_tokens")

@Singleton
class TokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val OWNER_ID = intPreferencesKey("owner_id")
    }

    val accessToken: Flow<String?> = context.tokenDataStore.data
        .map { it[ACCESS_TOKEN] }

    val refreshToken: Flow<String?> = context.tokenDataStore.data
        .map { it[REFRESH_TOKEN] }

    val ownerId: Flow<Int?> = context.tokenDataStore.data
        .map { it[OWNER_ID] }

    suspend fun saveTokens(accessToken: String, refreshToken: String, ownerId: Int) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[OWNER_ID] = ownerId
        }
    }

    suspend fun getAccessToken(): String? = accessToken.first()
    suspend fun getRefreshToken(): String? = refreshToken.first()
    suspend fun getOwnerId(): Int? = ownerId.first()

    suspend fun clear() {
        context.tokenDataStore.edit { it.clear() }
    }

    private val LOGIN_KEY = stringPreferencesKey("login")

    val login: Flow<String?> = context.tokenDataStore.data
        .map { it[LOGIN_KEY] }

    suspend fun getLogin(): String? = login.first()

    suspend fun saveTokens(accessToken: String, refreshToken: String, ownerId: Int, login: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[OWNER_ID] = ownerId
            prefs[LOGIN_KEY] = login  // ← добавили
        }
    }

}