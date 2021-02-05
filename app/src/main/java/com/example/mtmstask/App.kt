package com.example.mtmstask

import android.app.Application
import com.example.mtmstask.di.AppModule
import com.example.mtmstask.di.DaggerHomeActivityComponent
import com.example.mtmstask.di.HomeActivityComponent
import com.example.mtmstask.di.HomeActivityModule

class App :Application() {

    lateinit var homeActivityComponent: HomeActivityComponent
    override fun onCreate() {
        super.onCreate()
         homeActivityComponent= DaggerHomeActivityComponent
             .builder()
             .appModule(AppModule(this))
             .build()
    }
}