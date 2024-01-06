package com.example.muzik.ui.fragment.library_fragment.lib_playlist_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class PlaylistViewModel : ViewModel() {
    private val _playlists: MutableLiveData<List<Playlist>> = MutableLiveData(mutableListOf())
    val playlists = _playlists as LiveData<List<Playlist>>

    suspend fun fetchData() {
        try {
            muzikAPI.getTopPlaylists().body()?.let {
                val playlistsResponse = mutableListOf<Playlist>()
                for(i in it) playlistsResponse.add(Playlist.buildOnline(i))
                _playlists.value = playlistsResponse
            }
        } catch (e: Exception) {
            printLogcat(e)
        }
    }
}