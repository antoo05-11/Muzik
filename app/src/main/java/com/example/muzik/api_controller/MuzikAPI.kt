package com.example.muzik.api_controller

import com.example.muzik.data_model.retrofit_model.request.CreatePlaylistRequest
import com.example.muzik.data_model.retrofit_model.request.LoginRequest
import com.example.muzik.data_model.retrofit_model.request.PlaylistSongRequest
import com.example.muzik.data_model.retrofit_model.request.RegisterRequest
import com.example.muzik.data_model.retrofit_model.response.AlbumResponse
import com.example.muzik.data_model.retrofit_model.response.ArtistResponse
import com.example.muzik.data_model.retrofit_model.response.Chart
import com.example.muzik.data_model.retrofit_model.response.LoginResponse
import com.example.muzik.data_model.retrofit_model.response.PlaylistResponse
import com.example.muzik.data_model.retrofit_model.response.SearchResponse
import com.example.muzik.data_model.retrofit_model.response.SignUpResponse
import com.example.muzik.data_model.retrofit_model.response.SongResponse
import com.example.muzik.data_model.standard_model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface MuzikAPI {
    @GET("/api/song/{id}/info")
    suspend fun getSong(
        @Path("id") songID: String,
        @Query("youtube") youtube: Boolean? = null
    ): Response<SongResponse>

    @GET("/api/song/suggestSearch")
    suspend fun getSuggestions(
        @Query("q") q: String
    ): Response<List<String>>

    @GET("/api/song/search")
    suspend fun searchSong(
        @Query("youtube") youtube: Boolean? = null,
        @Query("next") next: Boolean? = null,
        @Query("searchText") searchText: String? = null
    ): Response<SearchResponse>

    @GET("/api/song/getAll")
    suspend fun getAllSongs(): Response<List<SongResponse>>

    @GET("/api/album/{id}/info")
    suspend fun getAlbum(@Path("id") albumID: Long): Response<List<SongResponse>>

    @GET("/api/album/getAll")
    suspend fun getAllAlbums(): Response<List<AlbumResponse>>

    @GET("/api/playlist/{id}/info")
    suspend fun getPlaylist(@Path("id") playListID: Long): Response<List<SongResponse>>

    @GET("/api/playlist/getAll")
    suspend fun getAllPlaylists(): Response<List<PlaylistResponse>>

    @GET("/api/artist/{id}/info")
    suspend fun getArtist(@Path("id") artistID: Long): Response<List<SongResponse>>

    @GET("/api/artist/getAll")
    suspend fun getAllArtists(): Response<List<ArtistResponse>>

    @GET("/api/user/{id}/info")
    suspend fun getUser(@Path("id") userID: Int): Response<User>

    @GET("/api/playlist/getTopPlaylists")
    suspend fun getTopPlaylists(): Response<List<PlaylistResponse>>

    @GET("/api/album/getRecentAlbums")
    suspend fun getRecentAlbums(): Response<List<AlbumResponse>>

    @GET("/api/artist/getYourArtists")
    suspend fun getYourArtists(): Response<List<ArtistResponse>>

    @GET("/api/song/getYourTopSongs")
    suspend fun getYourTopSongs(): Response<List<SongResponse>>

    @GET("/api/artist/{id}/artistAlbums")
    suspend fun getArtistAlbums(@Path("id") artistID: Long): Response<List<AlbumResponse>>

    @GET("/api/song/chart")
    suspend fun getSongCharts(): Response<List<Chart.SongWithView>>

    @POST("/api/auth/login")
    suspend fun userLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/playlist/{id}/add")
    suspend fun addSongToPlaylist(
        @Path("id") playlistID: Long,
        @Body playlistSongRequest: PlaylistSongRequest
    ): Response<PlaylistResponse>

    @POST("/api/playlist/create")
    suspend fun createPlaylist(@Body createPlaylistRequest: CreatePlaylistRequest, @Header("Authorization") authHeader: String): Response<PlaylistResponse>

     @POST("/api/user/create")
    suspend fun signUp(
        @Body registerRequest: RegisterRequest
    ): Call<SignUpResponse>

}
