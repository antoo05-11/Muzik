package com.example.muzik.ui.fragment.library_fragment.lib_song_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.music_service.LocalMusicRepository

class SongViewModel : ViewModel() {
    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs = _songs as LiveData<List<Song>>

    fun initSongs() {
        _songs.value = mutableListOf<Song>().apply { addAll(LocalMusicRepository.getSongs()) }
    }
}