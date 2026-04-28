package com.example.petpassport_android_app.di

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.hilt.ScreenModelKey
import com.example.petpassport_android_app.presentation.screens.events.EventsScreenModel
import com.example.petpassport_android_app.presentation.screens.events.eventDetails.EventDetailScreenModel
import com.example.petpassport_android_app.presentation.screens.home.PetListScreenModel
import com.example.petpassport_android_app.presentation.screens.login.LoginScreenModel
import com.example.petpassport_android_app.presentation.screens.medicalHistory.MedicalHistoryScreenModel
import com.example.petpassport_android_app.presentation.screens.petProfile.PetProfileScreenModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap


@Module
@InstallIn(SingletonComponent::class)
abstract class ScreenModelModule {

    @Binds
    @IntoMap
    @ScreenModelKey(LoginScreenModel::class)
    abstract fun bindLoginScreenModel(model: LoginScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(PetListScreenModel::class)
    abstract fun bindPetListScreenModel(model: PetListScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(PetProfileScreenModel::class)
    abstract fun bindPetProfileScreenModel(model: PetProfileScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(EventsScreenModel::class)
    abstract fun bindEventsScreenModel(model: EventsScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(MedicalHistoryScreenModel::class)
    abstract fun bindMedicalHistoryScreenModel(model: MedicalHistoryScreenModel): ScreenModel

    @Binds
    @IntoMap
    @ScreenModelKey(EventDetailScreenModel::class)
    abstract fun bindEventDetailScreenModel(model: EventDetailScreenModel): ScreenModel


}