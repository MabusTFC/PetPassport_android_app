package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.Event.VaccineDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VaccineApiService {
    @POST("/api/vaccines")
    suspend fun createVaccine(@Body vaccine: VaccineDto): VaccineDto

    @GET("/api/vaccines/{id}")
    suspend fun getVaccineById(@Path("id") id: Int): VaccineDto

    @PUT("/api/vaccines/{id}")
    suspend fun updateVaccine(@Path("id") id: Int, @Body vaccine: VaccineDto): VaccineDto

    @DELETE("/api/vaccines/{id}")
    suspend fun deleteVaccine(@Path("id") id: Int)

}