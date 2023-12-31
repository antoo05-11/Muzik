package com.example.muzik.ui.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.utils.printLogcat

class MainActivityViewModel : ViewModel() {
    private val _playlists: MutableLiveData<List<Playlist>> = MutableLiveData(mutableListOf())
    val playlists = _playlists as LiveData<List<Playlist>>

    suspend fun fetchData(accessToken: String) {
        try {
            val playlists = mutableListOf<Playlist>()
            MainActivity.muzikAPI.getUserPlaylists("Bearer $accessToken").body()?.let {
                for (i in it) playlists.add(Playlist.buildOnline(i))
            }
            _playlists.value = playlists
        } catch (e: Exception) {
            printLogcat(e)
        }
    }
}