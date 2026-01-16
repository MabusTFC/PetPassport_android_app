package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.User.PetDto
import com.example.petpassport_android_app.data.dto.User.PhotoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PetApiService {
    @POST("api/Pets")
    suspend fun createPet(@Body pet: PetDto): PetDto

    @POST("api/Pets/{petId}/upload")
    suspend fun uploadPet(@Path("petId") petId: Int, @Body photos: List<PhotoDto>): List<PhotoDto>

    @GET("api/Pets/{petId}")
    suspend fun getPet(@Path("petId") petId: Int): PetDto

    @PUT("api/Pets/{petId}")
    suspend fun updatePet(@Path("petId") petId: Int, @Body pet: PetDto): PetDto

    @PUT("api/Pets/{petId}/photos")
    suspend fun updatePetPhotos(@Path("petId") petId: Int, @Body photos: List<PhotoDto>): List<PhotoDto>
}