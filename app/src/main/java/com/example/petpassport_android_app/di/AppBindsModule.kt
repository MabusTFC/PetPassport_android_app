package com.example.petpassport_android_app.di

import com.example.petpassport_android_app.data.PetApiService
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class AppBindsModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:5000/:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providePetApi(retrofit: Retrofit): PetApiService =
        retrofit.create(PetApiService::class.java)
}