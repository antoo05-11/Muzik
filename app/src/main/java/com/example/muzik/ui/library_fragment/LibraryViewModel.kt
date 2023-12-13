package com.example.muzik.ui.library_fragment

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Artist
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
            val topPlaylistsRes = MainActivity.muzikAPI.getTopPlaylists()
            _topPlaylistsList.value = topPlaylistsRes.body()
        }
        lifecycleCoroutineScope.launch {
            val yourArtistsListRes = MainActivity.muzikAPI.getYourArtists()
            _yourArtistsList.value = yourArtistsListRes.body()
        }
    }
}