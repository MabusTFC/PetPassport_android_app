package com.example.petpassport_android_app.di

import android.content.Context
import android.content.SharedPreferences
import com.example.petpassport_android_app.BuildConfig
import com.example.petpassport_android_app.data.api.AuthApiService
import com.example.petpassport_android_app.data.api.DoctorVisitApiService
import com.example.petpassport_android_app.data.api.EventTemplateApiService
import com.example.petpassport_android_app.data.api.EventsApiService
import com.example.petpassport_android_app.data.api.OwnerApiService
import com.example.petpassport_android_app.data.api.PetApiService
import com.example.petpassport_android_app.data.api.TreatmentApiService
import com.example.petpassport_android_app.data.api.VaccineApiService
import com.example.petpassport_android_app.data.network.AuthInterceptor
import com.example.petpassport_android_app.data.network.TokenRefreshInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenRefreshInterceptor: TokenRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(tokenRefreshInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // ← принимает готовый клиент
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL.ensureTrailingSlash())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"

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

    //Шаблоны рекомендуемых процедур
    @Provides
    @Singleton
    fun provideEventTemplateApi(retrofit: Retrofit): EventTemplateApiService {
        return retrofit.create(EventTemplateApiService::class.java)
    }

    //Сохр данных в файл
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

}
