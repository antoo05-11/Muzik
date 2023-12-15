package com.example.muzik.ui.playlist_album_fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Song
import com.example.muzik.ui.main_activity.MainActivity

class PlaylistAlbumViewModel : ViewModel() {
    private val _playlistAlbumsList: MutableLiveData<List<Song>> = MutableLiveData()
    val playlistAlbumsList = _playlistAlbumsList as LiveData<List<Song>>

    enum class Type { PLAYLIST, ALBUM }

    suspend fun fetchSongs(playlistAlbumID: Int, type: Type, context: Context) {
        try {
            if (type == Type.PLAYLIST)
                _playlistAlbumsList.value = MainActivity.muzikAPI.getPlaylist(playlistAlbumID).body()
            else
                _playlistAlbumsList.value = MainActivity.muzikAPI.getAlbum(playlistAlbumID).body()
        }
        catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }
    }
}