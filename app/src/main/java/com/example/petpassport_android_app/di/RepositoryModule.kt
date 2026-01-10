package com.example.petpassport_android_app.di

import com.example.petpassport_android_app.data.repository.AuthRepositoryImpl
import com.example.petpassport_android_app.data.repository.EventMedicineRepositoryImpl
import com.example.petpassport_android_app.data.repository.OwnerRepositoryImpl
import com.example.petpassport_android_app.data.repository.PetRepositoryImpl
import com.example.petpassport_android_app.domain.repository.AuthRepository
import com.example.petpassport_android_app.domain.repository.EventMedicineRepository
import com.example.petpassport_android_app.domain.repository.OwnerRepository
import com.example.petpassport_android_app.domain.repository.PetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindOwnerRepository(
        ownerRepositoryImpl: OwnerRepositoryImpl
    ): OwnerRepository

    @Binds
    @Singleton
    abstract fun bindEventMedicineRepository(
        eventMedicineRepositoryImpl: EventMedicineRepositoryImpl
    ): EventMedicineRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

}