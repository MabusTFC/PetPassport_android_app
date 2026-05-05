package com.example.petpassport_android_app.data.network

import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.dto.User.RefreshRequestDto
import com.example.petpassport_android_app.data.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class TokenRefreshInterceptor @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val authApiProvider: Provider<AuthApiService> // Provider чтобы избежать circular dependency
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        // Не рефрешим если сам запрос на auth эндпоинты
        if (response.code != 401 ||
            chain.request().url.encodedPath.contains("api/v2/auth")) {
            return response
        }

        response.close()

        return runBlocking {
            val refreshToken = tokenStorage.getRefreshToken()
                ?: return@runBlocking chain.proceed(chain.request())

            try {
                val newTokens = authApiProvider.get().refresh(RefreshRequestDto(refreshToken))
                tokenStorage.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    ownerId = newTokens.ownerId
                )
                // Повторяем оригинальный запрос с новым токеном
                val newRequest = chain.request().newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
                chain.proceed(newRequest)
            } catch (e: Exception) {
                // Refresh не удался — чистим токены
                tokenStorage.clear()
                chain.proceed(chain.request())
            }
        }
    }
}