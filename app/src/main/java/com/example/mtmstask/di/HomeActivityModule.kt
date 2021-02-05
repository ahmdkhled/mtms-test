package com.example.mtmstask.di

import com.example.mtmstask.adapters.LocationsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule() {


    @Provides
    fun getDBInstance():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }


}