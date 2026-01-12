package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.dto.Auth.LoginDto
import com.example.petpassport_android_app.data.dto.Auth.RegisterDto
import com.example.petpassport_android_app.data.mapper.toDomain
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun register(login: String, password: String): Owner? {
        return try {
            api.register(RegisterDto(login, password)).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun login(login: String, password: String): Owner? {
        return try {
            api.login(LoginDto(login, password)).toDomain()
        } catch (e: Exception) {
            null
        }
    }
}


