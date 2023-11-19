package com.example.muzik.api_controller

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //private const val baseUrl = "http://10.0.2.2:6600/"
    private const val baseUrl = "http://192.168.33.103:6600/"
    //private const val baseUrl = "https://muzik-server-uet-i4e7.onrender.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}