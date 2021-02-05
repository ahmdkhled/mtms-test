package com.example.mtmstask.viewmodels

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.example.mtmstask.repo.LocationRepo
import com.example.mtmstask.repo.SourceRepo
import javax.inject.Inject

class HomeActivityVM @Inject constructor(val locationRepo: LocationRepo,val sourceRepo: SourceRepo):ViewModel() {

    suspend fun getLocation(): Res<Location> {
        return locationRepo.getLocation()
    }

     fun isLocationEnabled(): Boolean {
        return  locationRepo.isLocationEnabled()
    }
    suspend fun getLocations(): Res<ArrayList<Location>> {
        return sourceRepo.getLocations()
    }

    suspend fun getAutoCompleteListener(query: String): Res<ArrayList<Location>> {
        return locationRepo.getAutoComplete(query)
    }


    }