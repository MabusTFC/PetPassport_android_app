package com.example.petpassport_android_app.data.repository

import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.dto.User.LogoutRequestDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginRegisterDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginResultDto
import com.example.petpassport_android_app.data.storage.TokenStorage
import com.example.petpassport_android_app.domain.model.Owner
import com.example.petpassport_android_app.domain.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthException(message: String) : Exception(message)

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApiService,      // ← новый API
    private val tokenStorage: TokenStorage    // ← хранилище токенов
) : AuthRepository {

    override suspend fun login(login: String, password: String): Owner? {
        return try {
            val response = authApi.login(OwnerLoginRegisterDto(login.trim(), password))
            tokenStorage.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                ownerId = response.ownerId,
                login = login.trim()
            )
            Owner(id = response.ownerId, login = login.trim())
        } catch (e: HttpException) {
            throw AuthException(e.toUserMessage("Ошибка входа"))
        }
    }

    override suspend fun register(login: String, password: String): Owner? {
        return try {
            val response = authApi.register(OwnerLoginRegisterDto(login.trim(), password))
            tokenStorage.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                ownerId = response.ownerId,
                login = login.trim()
            )
            Owner(id = response.ownerId, login = login.trim())
        } catch (e: HttpException) {
            throw AuthException(e.toUserMessage("Ошибка регистрации"))
        }
    }

    override suspend fun logout() {
        try {
            val refreshToken = tokenStorage.getRefreshToken() ?: return
            authApi.logout(LogoutRequestDto(refreshToken))
        } catch (_: Exception) {
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

private fun HttpException.toUserMessage(defaultMessage: String): String {
    val body = response()?.errorBody()?.string()?.trim()?.trim('"')
    return when {
        !body.isNullOrBlank() -> body
        code() == 409 -> "Пользователь с таким логином уже существует"
        code() == 401 -> "Неверный логин или пароль"
        code() == 400 -> "Проверьте логин и пароль"
        else -> "$defaultMessage: HTTP ${code()}"
    }
}


