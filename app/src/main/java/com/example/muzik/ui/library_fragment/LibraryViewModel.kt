package com.example.muzik.ui.library_fragment

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private var _topPlaylistsList = MutableLiveData<List<Playlist>>()
    val topPlaylistsList = _topPlaylistsList as LiveData<List<Playlist>>

    private var _yourArtistsList = MutableLiveData<List<Artist>>()
    val yourArtistsList = _yourArtistsList as LiveData<List<Artist>>

    fun fetchData(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        lifecycleCoroutineScope.launch {
            try {
                val playlistList = mutableListOf<Playlist>()
                muzikAPI.getTopPlaylists().body()?.let {
                    for (i in it) playlistList.add(Playlist.buildOnline(i))
                }
                _topPlaylistsList.value = playlistList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }
        lifecycleCoroutineScope.launch {
            try {
                val artistList = mutableListOf<Artist>()
                MainActivity.muzikAPI.getYourArtists().body()?.let {
                    for (i in it) artistList.add(Artist.buildOnline(i))
                }
                _yourArtistsList.value = artistList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }
    }
}