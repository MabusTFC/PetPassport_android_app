package com.example.petpassport_android_app.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    //@ViewModelKey(PetViewModel::class) // Нужно создать аннотацию ViewModelKey
    abstract fun bindPetViewModel(viewModel: PetViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}