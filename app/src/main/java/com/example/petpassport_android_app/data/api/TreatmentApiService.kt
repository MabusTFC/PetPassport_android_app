package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.Event.TreatmentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TreatmentApiService {

    @POST("/api/treatments")
    suspend fun createTreatment(@Body treatment: TreatmentDto): TreatmentDto

    @GET("/api/treatments/{id}")
    suspend fun getTreatmentById(@Path("id") id: Int): TreatmentDto

    @PUT("/api/treatments/{id}")
    suspend fun updateTreatment(@Path("id") id: Int, @Body treatment: TreatmentDto): TreatmentDto

    @DELETE("/api/treatments/{id}")
    suspend fun deleteTreatment(@Path("id") id: Int)
}






