package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.dto.User.OwnerLoginRegisterDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginResultDto
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: OwnerApiService
) : AuthRepository {

    override suspend fun login(login: String, password: String): Owner? {
        try {
            val result = api.login(OwnerLoginRegisterDto(login, password))
            return Owner(id = result.ownerId, login = login)
        } catch (e: Exception) {
            return null
        }
    }

    override suspend fun register(login: String, password: String): Owner? {
        try {
            val response = api.register(OwnerLoginRegisterDto(login, password))
            val ownerId = response.trim().toInt()
            return Owner(id = ownerId, login = login)
        } catch (e: Exception) {
            return null
        }
    }
}



