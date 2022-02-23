package com.example.assessmenttask.service

import com.example.assessmenttask.models.Places
import com.google.android.gms.location.Geofence
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NearbyServiceApi {

    @GET
    fun getNearbyRestaurant(@Url url:String):Call<Places>


}