package com.example.muzik.api_controller

import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.Playlist
import com.example.muzik.response_model.Song
import com.example.muzik.response_model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MuzikAPI {
    @GET("/api/song/{id}/info")
    suspend fun getSong(@Path("id") songID: Int): Response<Song>

    @GET("/api/song/getAll")
    suspend fun getAllSongs(): Response<List<Song>>

    @GET("/api/album/{id}/info")
    suspend fun getAlbum(@Path("id") albumID: Int): Response<Album>

    @GET("/api/album/getAll")
    suspend fun getAllAlbums(): Response<List<Album>>

    @GET("/api/playlist/{id}/info")
    suspend fun getPlaylist(@Path("id") playListID: Int): Response<Playlist>

    @GET("/api/playlist/getAll")
    suspend fun getAllPlaylists(): Response<List<Playlist>>

    @GET("/api/artist/{id}/info")
    suspend fun getArtist(@Path("id") artistID: Int): Response<Artist>

    @GET("/api/artist/getAll")
    suspend fun getAllArtists(): Response<List<Artist>>

    @GET("/api/user/{id}/info")
    suspend fun getUser(@Path("id") userID: Int): Response<User>
}