package com.example.muzik.ui.fragment.playlist_album_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.activity.main_activity.MainActivity

class PlaylistAlbumViewModel : ViewModel() {
    private val _playlistAlbumsList: MutableLiveData<List<Song>> = MutableLiveData()
    val playlistAlbumsList = _playlistAlbumsList as LiveData<List<Song>>

    enum class Type { PLAYLIST, ALBUM }

    suspend fun fetchSongs(playlistAlbumID: Long, type: Type, artistName: String? = null) {
        try {
            if (type == Type.PLAYLIST) {
                val songList = mutableListOf<Song>()
                MainActivity.muzikAPI.getPlaylist(playlistAlbumID).body()?.let {
                    for (i in it) {
                        songList.add(Song.buildOnline(i))
                    }
                }
                _playlistAlbumsList.value = songList
            } else {
                val songList = mutableListOf<Song>()
                MainActivity.muzikAPI.getAlbumSongs(playlistAlbumID).body()?.let {
                    for (i in it) {
                        songList.add(Song.buildOnline(i))
                    }
                }
                _playlistAlbumsList.value = songList
            }
        } catch (e: Throwable) {
            Log.e("NETWORK_ERROR", e.message.toString())
        }
    }
}