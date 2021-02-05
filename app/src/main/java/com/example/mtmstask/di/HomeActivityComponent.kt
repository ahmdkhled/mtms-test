package com.example.mtmstask.di

import com.example.mtmstask.Utils
import com.example.mtmstask.view.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,VIewmodelModule::class,HomeActivityModule::class])
interface HomeActivityComponent {

    fun inject(activity : HomeActivity)

    fun getUtils():Utils{
        return Utils()
    }
}