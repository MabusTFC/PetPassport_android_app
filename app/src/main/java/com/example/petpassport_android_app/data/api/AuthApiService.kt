package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.Auth.LoginDto
import com.example.petpassport_android_app.data.dto.Auth.RegisterDto
import com.example.petpassport_android_app.data.dto.User.OwnerDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginDto): OwnerDto

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterDto): OwnerDto
}