package com.example.petpassport_android_app.di

import android.content.Context
import android.content.SharedPreferences
import com.example.petpassport_android_app.data.api.DoctorVisitApiService
import com.example.petpassport_android_app.data.api.EventsApiService
import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.data.api.TreatmentApiService
import com.example.petpassport_android_app.data.api.VaccineApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:7205/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Владелец
    @Provides
    @Singleton
    fun provideOwnerApi(retrofit: Retrofit): OwnerApiService {
        return retrofit.create(OwnerApiService::class.java)
    }

    //Питомцы
    @Provides
    @Singleton
    fun providePetApi(retrofit: Retrofit): PetApiService {
        return retrofit.create(PetApiService::class.java)
    }

    //Общие события
    @Provides
    @Singleton
    fun provideEventsApi(retrofit: Retrofit): EventsApiService {
        return retrofit.create(EventsApiService::class.java)
    }

    //Вакцины
    @Provides
    @Singleton
    fun provideVaccineApi(retrofit: Retrofit): VaccineApiService {
        return retrofit.create(VaccineApiService::class.java)
    }

    //Лечение
    @Provides
    @Singleton
    fun provideTreatmentApi(retrofit: Retrofit): TreatmentApiService {
        return retrofit.create(TreatmentApiService::class.java)
    }

    //Визиты к врачу
    @Provides
    @Singleton
    fun provideDoctorVisitApi(retrofit: Retrofit): DoctorVisitApiService {
        return retrofit.create(DoctorVisitApiService::class.java)
    }


    //Сохр данных в файл
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

}