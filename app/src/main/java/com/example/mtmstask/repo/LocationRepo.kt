package com.example.mtmstask.repo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.location.LocationManager
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import com.example.mtmstask.R
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



class LocationRepo {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(
        ctx: Context
    )= suspendCoroutine<Res<Location>> { cont->

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)

        mFusedLocationClient.lastLocation.addOnCompleteListener {task->
            val result=task.result
            if (task.isSuccessful&&result!=null){
                cont.resume(Res.SUCCCESS(Location(result.longitude,result.latitude)))
            }else{
                requestNewLocationData(cont,ctx)
                Log.d("TAG", "getLocation repo: ${task.result}")
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(cont: Continuation<Res<Location>>, ctx:Context) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
            object :LocationCallback(){
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    Log.d("TAG", "onLocationResult: ${result?.lastLocation}")
                    Log.d("TAG", "onLocationResult: ")
                    val longitude=result?.lastLocation?.longitude
                    val latitude=result?.lastLocation?.latitude
                    if (longitude!=null&&latitude!=null){
                        cont.resume(Res.SUCCCESS(
                            Location(longitude, latitude)
                        ))
                    }else{
                        cont.resume(Res.ERROR<Location>(null,"error getting location"))
                    }

                }


            }

            , null);
    }

     fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    suspend fun getAutoComplete(context:Context,query: String): Res<ArrayList<Location>> {
        Places.initialize(context, context.getString(R.string.google_maps_key));
        val placesClient = Places.createClient(context);

        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountries()
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()
        try {
            val result=placesClient.findAutocompletePredictions(request)
                .await()

            val predictions=result.autocompletePredictions
            val locations=ArrayList<Location>()
            for (prediction in predictions){
                locations.add(Location(null,null,prediction.getFullText(StyleSpan(Typeface.BOLD)).toString()))
            }
            Log.d("TAG", "getAutoComplete: ${result.autocompletePredictions}")
            return Res.SUCCCESS(locations)

        }catch (ex:Exception){
            Log.d("TAG", "getAutoComplete: ${ex.message}")
            return Res.ERROR(null,"error getting suggesstions")
        }
    }
}