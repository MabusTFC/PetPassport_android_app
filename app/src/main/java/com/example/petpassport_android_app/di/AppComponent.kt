package com.example.petpassport_android_app.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(
    modules = [AppBindsModule::class]
)
@Singleton
abstract class AppComponent{
    //abstract fun inject(fragment: MainFragment)
    //abstract fun inject(fragment: CreateTaskFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}

@Module(
    includes = [
        //AppBindsModule::class,
        //ViewModelModule::class
    ]
) class AppModule