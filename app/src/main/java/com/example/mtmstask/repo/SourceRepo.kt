package com.example.mtmstask.repo

import android.util.Log
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SourceRepo {

    suspend fun getLocations(): Res<ArrayList<Location>> {
        val db=FirebaseFirestore.getInstance()

        try {

            val result=db.collection("source")
                .get()
                .await()

            if (result!=null){
                val locations=ArrayList<Location>()
                for (doc in result.documents){
                    val location=doc.toObject(Location::class.java)
                    if (location != null) {
                        locations.add(location)
                    }
                }
                return Res.SUCCCESS(locations)
            }else{
                return Res.ERROR(null,"error getting locations ....")
            }

        }catch (ex:Exception){
            Log.d("TAG", "getLocations: err ${ex.message}")
            return Res.ERROR(null,"error getting locations")
        }

    }
}