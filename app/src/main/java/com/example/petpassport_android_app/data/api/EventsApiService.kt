package com.example.petpassport_android_app.data.api

import com.example.petpassport_android_app.data.dto.EventDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EventsApiService {

    @GET("api/events/upcoming/{petId}")
    suspend fun getUpcomingEvents(@Path("petId") petId: Int): List<EventDto>


}