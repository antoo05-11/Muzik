package com.example.muzik.api_controller

import com.example.muzik.api_controller.response_model.Song
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MuzikAPI {
    @GET("/api/song/{id}/info")
    suspend fun getSong(@Path("id") songID: Int): Response<Song>
}