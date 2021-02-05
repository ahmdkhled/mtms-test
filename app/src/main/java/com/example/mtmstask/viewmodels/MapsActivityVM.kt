package com.example.mtmstask.viewmodels

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.example.mtmstask.repo.LocationRepo
import com.example.mtmstask.repo.SourceRepo

class MapsActivityVM :ViewModel() {

    suspend fun getLocation(context: Context): Res<Location> {
        return LocationRepo().getLocation(context)
    }

     fun isLocationEnabled(context: Context): Boolean {
        return  LocationRepo().isLocationEnabled(context)
    }
    suspend fun getLocations(): Res<ArrayList<Location>> {
        return SourceRepo().getLocations()
    }

    suspend fun getAutoCompleteListener(context:Context,query: String): Res<ArrayList<Location>> {
        return LocationRepo().getAutoComplete(context,query)
    }


    }