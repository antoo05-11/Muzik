package com.example.muzik.ui.fragment.library_fragment.lib_album_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.ui.activity.main_activity.MainActivity
import com.example.muzik.utils.printLogcat

class AlbumViewModel : ViewModel() {
    private val _albums: MutableLiveData<List<Album>> = MutableLiveData(mutableListOf())
    val albums = _albums as LiveData<List<Album>>

    suspend fun fetchData() {
        try {
            MainActivity.muzikAPI.getRecentAlbums().body()?.let {
                val albumsResponse = mutableListOf<Album>()
                for (i in it) albumsResponse.add(Album.buildOnline(i))
                _albums.value = albumsResponse
            }
        } catch (e: Exception) {
            printLogcat(e)
        }
    }
}