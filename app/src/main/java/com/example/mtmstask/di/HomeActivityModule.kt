package com.example.mtmstask.di

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtmstask.adapters.LocationsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule(val context: Context) {


    @Provides
    fun getDBInstance():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    fun geLayoutManager():LinearLayoutManager{
        return LinearLayoutManager(context)
    }




}