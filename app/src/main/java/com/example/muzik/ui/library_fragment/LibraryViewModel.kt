package com.example.muzik.ui.library_fragment

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.response_model.Playlist
import com.example.muzik.ui.main_activity.MainActivity
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {
    private var _topPlaylistsList = MutableLiveData<List<Playlist>>()
    val topPlaylistsList = _topPlaylistsList as LiveData<List<Playlist>>

    private var _yourArtistsList = MutableLiveData<List<Artist>>()
    val yourArtistsList = _yourArtistsList as LiveData<List<Artist>>

    fun fetchData(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        lifecycleCoroutineScope.launch {
            try {
                val topPlaylistsRes = MainActivity.muzikAPI.getTopPlaylists()
                _topPlaylistsList.value = topPlaylistsRes.body()
            }
            catch (e: Throwable) {
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