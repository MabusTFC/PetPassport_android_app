package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.User.PetDto
import com.example.petpassport_android_app.data.dto.User.PhotoDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PetApiService {
    @POST("api/Pets")
    suspend fun createPet(@Body pet: PetDto): PetDto

    @GET("api/Pets/{petId}")
    suspend fun getPet(@Path("petId") petId: Int): PetDto

    @PUT("api/Pets/{petId}")
    suspend fun updatePet(@Path("petId") petId: Int, @Body pet: PetDto): PetDto

    @Multipart
    @PUT("api/Pets/{petId}/photos")
    suspend fun uploadPetPhoto(
        @Path("petId") petId: Int,
        @Part newFiles: List<MultipartBody.Part>,
        @Part("deletePhotoIds") deletePhotoIds: List<Int>? = null
    ): Response<PhotoDto>
}