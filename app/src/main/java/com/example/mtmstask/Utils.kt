package com.example.mtmstask

import android.content.Context
import android.net.ConnectivityManager
import com.example.mtmstask.model.Location
import javax.inject.Inject

class Utils @Inject constructor() {

    lateinit var result:Pair<Double,Location>

    fun getShortestDistance(source: Location,locations:ArrayList<Location>): Pair<Double, Location> {

        var lowestDistance=Double.POSITIVE_INFINITY
        var lowestLocation=Location()

        for ( location in locations){
            val distance=distance(source.latitude,source.longitude,location.latitude,location.longitude)
            if (distance<lowestDistance){
                lowestDistance=distance
                lowestLocation=location
            }
        }
         result=Pair(lowestDistance,lowestLocation)
        return result
    }

    private fun distance(
        lat1: Double?,
        lon1: Double?,
        lat2: Double?,
        lon2: Double?
    ): Double {
        if (lat1==null||lon1==null||lat2==null||lon2==null){
            return Double.POSITIVE_INFINITY
        }

        val theta = lon1 - lon2
        var dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(
                deg2rad(lat1)
            ) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.isActiveNetworkMetered()

    }
}