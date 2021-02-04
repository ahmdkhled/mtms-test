package com.example.mtmstask.repo

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.google.android.gms.location.*
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
}