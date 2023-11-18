package com.example.muzik.ui.artist_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.Song
import com.example.muzik.ui.main_activity.MainActivity

class ArtistViewModel : ViewModel() {
    private val _artist: MutableLiveData<Artist> = MutableLiveData()
    val artist = _artist as LiveData<Artist>

    private val _artistSongs: MutableLiveData<List<Song>> = MutableLiveData()
    val artistSongs = _artistSongs as LiveData<List<Song>>

    private val _artistAlbums: MutableLiveData<List<Album>> = MutableLiveData()
    val artistAlbums = _artistAlbums as LiveData<List<Album>>

    suspend fun fetchArtistSongs(artistID: Int) {
        _artistSongs.value = MainActivity.muzikAPI.getArtist(artistID).body()
    }

    suspend fun fetchArtistAlbums(artistID: Int) {
        _artistAlbums.value = MainActivity.muzikAPI.getArtistAlbums(artistID).body()
    }
}