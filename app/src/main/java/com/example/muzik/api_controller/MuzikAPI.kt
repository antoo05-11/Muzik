package com.example.muzik.api_controller

import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.LoginResponse
import com.example.muzik.response_model.Playlist
import com.example.muzik.response_model.SignUpResponse
import com.example.muzik.response_model.Song
import com.example.muzik.response_model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.Date

interface MuzikAPI {
    @GET("/api/song/{id}/info")
    suspend fun getSong(@Path("id") songID: Int): Response<Song>

    @GET("/api/song/getAll")
    suspend fun getAllSongs(): Response<List<Song>>

    @GET("/api/album/{id}/info")
    suspend fun getAlbum(@Path("id") albumID: Int): Response<List<Song>>

    @GET("/api/album/getAll")
    suspend fun getAllAlbums(): Response<List<Album>>

    @GET("/api/playlist/{id}/info")
    suspend fun getPlaylist(@Path("id") playListID: Int): Response<List<Song>>

    @GET("/api/playlist/getAll")
    suspend fun getAllPlaylists(): Response<List<Playlist>>

    @GET("/api/artist/{id}/info")
    suspend fun getArtist(@Path("id") artistID: Int): Response<List<Song>>

    @GET("/api/artist/getAll")
    suspend fun getAllArtists(): Response<List<Artist>>

    @GET("/api/user/{id}/info")
    suspend fun getUser(@Path("id") userID: Int): Response<User>

    @GET("/api/playlist/getTopPlaylists")
    suspend fun getTopPlaylists(): Response<List<Playlist>>

    @GET("/api/album/getRecentAlbums")
    suspend fun getRecentAlbums(): Response<List<Album>>

    @GET("/api/artist/getYourArtists")
    suspend fun getYourArtists(): Response<List<Artist>>

    @GET("/api/song/getYourTopSongs")
    suspend fun getYourTopSongs(): Response<List<Song>>

    @GET("/api/artist/{id}/artistAlbums")
    suspend fun getArtistAlbums(@Path("id") artistID: Int): Response<List<Album>>

    @FormUrlEncoded
    @POST("/api/auth/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("/api/auth/signup")
    fun signUp(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("phoneNumber") phoneNumber: Int,
        @Field("dateOfBirth") date: Date,
        @Field("password") password: String
    ): Call<SignUpResponse>
}
