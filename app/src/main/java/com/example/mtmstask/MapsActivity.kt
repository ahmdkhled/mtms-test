package com.example.mtmstask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mtmstask.viewmodels.MapsActivityVM
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_REQUEST_CODE=22
    private val PERMISSION_ID=11
    private lateinit var mMap: GoogleMap
    lateinit var mapsActivityVm:MapsActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapsActivityVm=ViewModelProvider(this).get(MapsActivityVM::class.java)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        requestPermission()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()

            }
        }
    }



    fun getLocation(){
        if (!mapsActivityVm.isLocationEnabled(this)){
            Toast.makeText(this, "Please turn on your location ", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent,LOCATION_REQUEST_CODE)
            return
        }


        lifecycleScope.launch(
           Dispatchers.IO
        ){


            val result=mapsActivityVm.getLocation(applicationContext)
            Log.d("TAG", "getLocation : $result")
            val location=result.res
            if (location!=null){
                val currentLocation = LatLng(location.latitude, location.longtude)
                Log.d("TAG", "currentLocation: $currentLocation")
                withContext(Dispatchers.Main){
                    mMap.addMarker(MarkerOptions().position(currentLocation))
                    val yourLocation = CameraUpdateFactory.newLatLngZoom(currentLocation, 20f)
                    mMap.animateCamera(yourLocation)
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

                }
            }



        }
    }
}