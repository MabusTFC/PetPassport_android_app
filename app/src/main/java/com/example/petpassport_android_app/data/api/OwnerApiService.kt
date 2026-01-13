package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.User.OwnerDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginRegisterDto
import com.example.petpassport_android_app.data.dto.User.OwnerLoginResultDto
import com.example.petpassport_android_app.data.dto.User.PetDto
import com.example.petpassport_android_app.data.dto.User.PetSummaryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OwnerApiService {

    @POST("api/owners/register-login")
    suspend fun register(@Body dto: OwnerLoginRegisterDto): String

    @POST("api/owners/login")
    suspend fun login(@Body dto: OwnerLoginRegisterDto): OwnerLoginResultDto

    @GET("api/owners/by-login/{login}")
    suspend fun getOwnerIdByLogin(@Path("login") login: String): String

    @GET("api/owners/{ownerId}/pets")
    suspend fun getPets(@Path("ownerId") ownerId: Int): List<PetSummaryDto>
}

