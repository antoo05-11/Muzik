package com.example.muzik.ui.artist_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity

class ArtistViewModel : ViewModel() {
    private val _artist: MutableLiveData<Artist> = MutableLiveData()
    val artist = _artist as LiveData<Artist>

    private val _artistSongs: MutableLiveData<List<Song>> = MutableLiveData()
    val artistSongs = _artistSongs as LiveData<List<Song>>

    private val _artistAlbums: MutableLiveData<List<Album>> = MutableLiveData()
    val artistAlbums = _artistAlbums as LiveData<List<Album>>

    suspend fun fetchArtistSongs(artistID: Long) {
        try {
            val songList = mutableListOf<Song>()
            MainActivity.muzikAPI.getArtist(artistID).body()?.let {
                for (i in it) {
                    songList.add(Song.buildOnline(i))
                }
            }
            _artistSongs.value = songList
        } catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }
    }

    suspend fun fetchArtistAlbums(artistID: Long) {
        try {
            val albumList = mutableListOf<Album>()
            MainActivity.muzikAPI.getArtistAlbums(artistID).body()?.let {
                for (i in it) albumList.add(Album.buildOnline(i))
            }
            _artistAlbums.value = albumList
        } catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }
    }
}