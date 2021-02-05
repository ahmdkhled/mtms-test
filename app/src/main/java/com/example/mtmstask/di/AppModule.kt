package com.example.mtmstask.di


import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var application: Application) {

    @Singleton
    @Provides
    fun getApp(): Application {
        return application
    }

    @Provides
    fun getContext():Context{
        return application.applicationContext
    }


}