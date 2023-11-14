package com.example.muzik.api_controller

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MuzikAPI {
    @GET("/api/song/{id}/info")
    suspend fun getSong(@Path("id") songID: Int): Response<com.example.muzik.response_model.Song>

    //@GET("/api/song/getAll")

    //@GET("/api/album/{id}/info")

    //@GET("/api/album/getAll")

    //@GET("/api/playlist/{id}/info")

    //@GET("/api/playlist/getAll")

    //@GET("/api/artist/{id}/info")

    //@GET("/api/artist/getAll")

    //@GET("/api/artist/getAll")

    //@GET("/api/user/{id}/info")
}