package com.example.mtmstask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mtmstask.R
import com.example.mtmstask.databinding.LocationItemBinding
import com.example.mtmstask.model.Location
import javax.inject.Inject

class LocationsAdapter @Inject constructor()
    :RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>(){

     var locations=ArrayList<Location>()
    lateinit var onLocationClickedListener:OnLocationClickedListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding=DataBindingUtil.inflate<LocationItemBinding>(LayoutInflater.from(parent.context),
            R.layout.location_item,parent,false)
        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.binding.location=locations[position]
        onLocationClickedListener.onLocationClicked(locations[position])
    }

    fun addLocations(locations: ArrayList<Location>?){
        if (locations==null)return
        this.locations.clear()
        this.locations.addAll(locations)
        notifyDataSetChanged()
    }

    fun setOnLocationCLicked(onLocationClickedListener: OnLocationClickedListener){
        this.onLocationClickedListener=onLocationClickedListener

    }

    class LocationViewHolder(val binding: LocationItemBinding) :RecyclerView.ViewHolder(binding.root){

    }

    interface OnLocationClickedListener{
        fun onLocationClicked(location:Location)
    }
}