package com.example.mtmstask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mtmstask.R
import com.example.mtmstask.databinding.LocationItemBinding
import com.example.mtmstask.model.Location

class LocationsAdapter :RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>(){

     var locations=ArrayList<Location>()

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
    }

    fun addLocations(locations: ArrayList<Location>?){
        if (locations==null)return
        this.locations.addAll(locations)
        notifyItemRangeInserted(0,locations.size-1)
    }

    class LocationViewHolder(val binding: LocationItemBinding) :RecyclerView.ViewHolder(binding.root){

    }
}