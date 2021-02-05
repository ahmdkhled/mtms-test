package com.example.mtmstask.repo

import android.util.Log
import com.example.mtmstask.model.Location
import com.example.mtmstask.model.Res
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SourceRepo @Inject constructor(val db :FirebaseFirestore) {

    suspend fun getLocations(collection:String): Res<ArrayList<Location>> {


        try {

            val result=db.collection(collection)
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