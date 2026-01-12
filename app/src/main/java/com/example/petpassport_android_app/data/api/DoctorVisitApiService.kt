package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.Event.DoctorVisitDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DoctorVisitApiService {
    @POST("/api/doctor-visit")
    suspend fun createDoctorVisit(@Body doctorVisit: DoctorVisitDto): DoctorVisitDto

    @GET("/api/doctor-visit/{id}")
    suspend fun getDoctorVisitById(@Path("id") id: Int): DoctorVisitDto

    @PUT("/api/doctor-visit/{id}")
    suspend fun updateDoctorVisit(@Path("id") id: Int, @Body doctorVisit: DoctorVisitDto): DoctorVisitDto

    @DELETE("/api/doctor-visit/{id}")
    suspend fun deleteDoctorVisit(@Path("id") id: Int)

}