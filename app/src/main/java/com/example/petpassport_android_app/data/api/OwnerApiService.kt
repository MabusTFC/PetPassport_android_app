package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.OwnerDto
import com.example.petpassport_android_app.data.dto.PetDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OwnerApiService {
    @POST("/api/Owners/register")
    suspend fun registerOwner(@Body owner: OwnerDto): OwnerDto

    @GET("api/Owners/{telegramId}/pets")
    suspend fun getPetsByOwner(@Path("telegramId") telegramId: String): List<PetDto>

    @GET("api/Owners/by-telegram/{telegramId}")
    suspend fun getOwnerByTelegramId(@Path("telegramId") telegramId: String): OwnerDto
}