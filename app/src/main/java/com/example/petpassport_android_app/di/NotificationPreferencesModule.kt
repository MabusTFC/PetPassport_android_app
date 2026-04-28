package com.example.petpassport_android_app.di

import com.example.petpassport_android_app.data.repository.NotificationPreferencesRepository as NotificationPreferencesRepositoryImpl
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationPreferencesModule {

    @Provides
    @Singleton
    fun provideNotificationPreferencesRepository(
        @ApplicationContext context: Context
    ): NotificationPreferencesRepository {
        return NotificationPreferencesRepositoryImpl(context)
    }
}