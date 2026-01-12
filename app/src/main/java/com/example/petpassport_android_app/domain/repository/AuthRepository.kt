package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.domain.model.Owner

interface AuthRepository {
    suspend fun login(login: String, password: String): Owner?
    suspend fun register(login: String, password: String): Owner?
}
