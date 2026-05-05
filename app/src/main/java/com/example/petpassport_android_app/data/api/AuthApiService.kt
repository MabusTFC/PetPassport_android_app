package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.User.AuthResponseDto
import com.example.petpassport_android_app.data.dto.User.LogoutRequestDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginRegisterDto
import com.example.petpassport_android_app.data.dto.User.RefreshRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v2/auth/register")
    suspend fun register(@Body dto: OwnerLoginRegisterDto): AuthResponseDto

    @POST("api/v2/auth/login")
    suspend fun login(@Body dto: OwnerLoginRegisterDto): AuthResponseDto

    @POST("api/v2/auth/refresh")
    suspend fun refresh(@Body dto: RefreshRequestDto): AuthResponseDto

    @POST("api/v2/auth/logout")
    suspend fun logout(@Body dto: LogoutRequestDto)
}