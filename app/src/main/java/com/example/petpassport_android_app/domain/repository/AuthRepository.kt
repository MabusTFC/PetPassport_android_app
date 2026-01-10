package com.example.petpassport_android_app.domain.repository

import com.example.petpassport_android_app.data.dto.AuthStatusDto

interface AuthRepository {
    suspend fun startLoginSession(): String
    suspend fun checkLoginStatus(uuid: String): AuthStatusDto
}