package com.example.assessmenttask.service

import com.example.assessmenttask.utils.Credentials
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getApiService():NearbyServiceApi{
          return Retrofit.Builder()
                .baseUrl(Credentials().Baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                 .build()
                .create(NearbyServiceApi::class.java)
        }

    }

