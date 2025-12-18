package com.example.petpassport_android_app.data.api

import retrofit2.http.GET

interface PetApiService {
    @GET("api/pets")
    suspend fun getPets(): List<Object>
}