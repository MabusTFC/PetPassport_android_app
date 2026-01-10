package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.AuthInitDto
import com.example.petpassport_android_app.data.dto.AuthStatusDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {
    @POST("api/auth/init")
    suspend fun initAuth(): AuthInitDto

    @GET("api/auth/check/{uuid}")
    suspend fun checkAuthStatus(@Path("uuid") uuid: String): AuthStatusDto
}