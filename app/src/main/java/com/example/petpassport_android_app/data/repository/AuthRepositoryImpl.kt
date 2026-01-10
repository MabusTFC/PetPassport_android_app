package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.dto.AuthStatusDto
import com.example.petpassport_android_app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {
    override suspend fun startLoginSession(): String = api.initAuth().uuid

    override suspend fun checkLoginStatus(uuid: String): AuthStatusDto {
        return api.checkAuthStatus(uuid)
    }
}
