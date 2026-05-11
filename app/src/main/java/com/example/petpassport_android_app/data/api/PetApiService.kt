package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.User.PetDto
import com.example.petpassport_android_app.data.dto.User.PetUpdateDto
import com.example.petpassport_android_app.data.dto.User.PhotoDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PetApiService {
    @Multipart
    @POST("api/v2/pets")
    suspend fun createPet(
        @Part("Name") name: RequestBody,
        @Part("Breed") breed: RequestBody,
        @Part("WeightKg") weightKg: RequestBody,
        @Part("BirthDate") birthDate: RequestBody,
    ): Int

    @GET("api/v2/pets/{petId}")
    suspend fun getPet(@Path("petId") petId: Int): PetDto

    @PUT("api/v2/pets/{petId}")
    suspend fun updatePet(@Path("petId") petId: Int, @Body pet: PetUpdateDto): Response<Unit>

    @Multipart
    @POST("api/v2/pets/{petId}/upload")
    suspend fun uploadPetPhoto(
        @Path("petId") petId: Int,
        @Part file: MultipartBody.Part,
    ): Response<PhotoDto>

    @Multipart
    @PUT("api/v2/pets/{petId}/photos")
    suspend fun replacePetPhotos(
        @Path("petId") petId: Int,
        @Part newFiles: List<MultipartBody.Part>,
        @Part("deletePhotoIds") deletePhotoIds: List<Int>,
    ): Response<Unit>

    @DELETE("api/v2/pets/{petId}/photos/{photoId}")
    suspend fun deletePetPhoto(
        @Path("petId") petId: Int,
        @Path("photoId") photoId: Int,
    ): Response<Unit>
}
