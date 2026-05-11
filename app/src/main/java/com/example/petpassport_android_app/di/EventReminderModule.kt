package com.example.petpassport_android_app.di

import com.example.petpassport_android_app.data.repository.EventReminderRepositoryImpl
import com.example.petpassport_android_app.domain.repository.EventReminderRepository
import com.example.petpassport_android_app.domain.repository.NotificationPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventReminderModule {
    @Binds
    @Singleton
    abstract fun bindEventReminderRepository(
        impl: EventReminderRepositoryImpl,
    ): EventReminderRepository
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ReminderBootEntryPoint {
    fun notificationPreferencesRepository(): NotificationPreferencesRepository
    fun eventReminderRepository(): EventReminderRepository
}
