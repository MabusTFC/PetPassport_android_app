package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.dto.User.LogoutRequestDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginRegisterDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginResultDto
import com.example.petpassport_android_app.data.storage.TokenStorage
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,      // ← новый API
    private val tokenStorage: TokenStorage    // ← хранилище токенов
) : AuthRepository {

    override suspend fun login(login: String, password: String): Owner? {
        return try {
            val response = authApi.login(OwnerLoginRegisterDto(login, password))
            tokenStorage.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                ownerId = response.ownerId,
                login = login
            )
            Owner(id = response.ownerId, login = login)
        } catch (e: Exception) { null }
    }

    override suspend fun register(login: String, password: String): Owner? {
        return try {
            val response = authApi.register(OwnerLoginRegisterDto(login, password))
            tokenStorage.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                ownerId = response.ownerId,
                login = login
            )
            Owner(id = response.ownerId, login = login)
        } catch (e: Exception) { null }
    }

    override suspend fun logout() {
        try {
            val refreshToken = tokenStorage.getRefreshToken() ?: return
            authApi.logout(LogoutRequestDto(refreshToken))
        } finally {
            tokenStorage.clear()
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenStorage.getAccessToken() != null
    }

    override suspend fun getOwnerId(): Int? {
        return tokenStorage.getOwnerId()
    }

    override suspend fun getLogin(): String? = tokenStorage.getLogin()

}




