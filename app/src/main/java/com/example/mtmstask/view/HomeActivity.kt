package com.example.mtmstask.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mtmstask.App
import com.example.mtmstask.R
import com.example.mtmstask.Utils
import com.example.mtmstask.adapters.LocationsAdapter
import com.example.mtmstask.databinding.ActivityHomeBinding
import com.example.mtmstask.model.Location
import com.example.mtmstask.viewmodels.HomeActivityVM
import com.example.mtmstask.viewmodels.HomeActivityVMFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), OnMapReadyCallback,LocationsAdapter.OnLocationClickedListener {

    private val LOCATION_REQUEST_CODE=22
    private val PERMISSION_ID=11
    private lateinit var mMap: GoogleMap
    lateinit var homeActivityVm:HomeActivityVM
    lateinit var binding:ActivityHomeBinding
    @Inject lateinit var factory:HomeActivityVMFactory
    @Inject lateinit var adapter:LocationsAdapter
    @Inject lateinit var layoutManager:LinearLayoutManager
    @Inject lateinit var utils : Utils
    var type="source"
    lateinit var source:Location
    lateinit var destination:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,
            R.layout.activity_home
        )
        (application as App).homeActivityComponent.inject(this)


        homeActivityVm=ViewModelProvider(this,factory).get(HomeActivityVM::class.java)


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.mapsView.toggle.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)

        }

        requestPermission()


        binding.mapsView.locationsRecycler.adapter=adapter
        binding.mapsView.locationsRecycler.layoutManager=layoutManager
        adapter.setOnLocationCLicked(this)

        binding.mapsView.myLocationIL
            .editText
            ?.setOnFocusChangeListener{ view: View, b: Boolean ->
                Log.d("TAG", "foucused: $b")
                if (b) getLocations()
            }

        binding.mapsView.destinationIL
            .editText
            ?.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    lifecycleScope.launch {
                        val res=homeActivityVm.getAutoCompleteListener(s.toString())
                        Log.d("TAG", "onTextChanged: $res")
                        if (res.success){
                            type="destination"
                            val predictions=res.res
                            if(predictions!=null){
                                binding.mapsView.locationsRecycler.visibility=View.VISIBLE

                                adapter.addLocations(predictions)
                            }
                        }

                    }
                }

            })


        binding.mapsView.requestRD.setOnClickListener {
            getDriversLocations()
        }

    }

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
                getCurrentLocation()

            }
        }
    }



    fun getCurrentLocation(){
        if (!homeActivityVm.isLocationEnabled()){
            Toast.makeText(this, "Please turn on your location ", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent,LOCATION_REQUEST_CODE)
            return
        }


        lifecycleScope.launch(
           Dispatchers.IO
        ){


            val result=homeActivityVm.getLocation()
            Log.d("TAG", "getLocation : $result")
            val location=result.res

            if (location!=null){
                val lat=location.latitude
                val lng=location.longitude
                if (lat==null||lng==null){
                    return@launch
                }

                val currentLocation = LatLng(lat, lng)
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

    fun getLocations(){
        lifecycleScope.launch {
            Log.d("TAG", "getLocations: ")
            val res=homeActivityVm.getLocations("source")
            Log.d("TAG", "getLocations: $res")
            if (res.success&&res.res!=null){
                type="source"
                val locations=res.res
                binding.mapsView.locationsRecycler.visibility=View.VISIBLE
                adapter.addLocations(locations)
            }
        }
    }

    override fun onLocationClicked(location: Location) {
        binding.mapsView.locationsRecycler.visibility=View.GONE
        if (type.equals("source")){
            source=location
        }else
            destination=location
    }
    fun getDriversLocations(){
        lifecycleScope.launch {
            val res=homeActivityVm.getLocations("drivers")
            if (res.success&&res.res!=null){
                val locations=res.res
                if (locations!=null&&::source.isInitialized){
                    val result=utils.getShortestDistance(source,locations)
                    Log.d("TAG", "getDriversLocations: $result")
                    Toast.makeText(this@HomeActivity,result.second.name,Toast.LENGTH_LONG).show()


                }else
                    Toast.makeText(this@HomeActivity,"error getting best RD",Toast.LENGTH_LONG).show()
            }
        }
    }
}