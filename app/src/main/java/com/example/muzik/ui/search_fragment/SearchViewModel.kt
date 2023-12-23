package com.example.muzik.ui.search_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class SearchViewModel : ViewModel() {
    private val _songList: MutableLiveData<List<Song>> = MutableLiveData()
    val songList = _songList as LiveData<List<Song>>

    suspend fun fetchSearchSongs(youtube: Boolean? = null, searchText: String? = null) {
        try {
            val songs = mutableListOf<Song>()
            muzikAPI.searchSong(youtube = youtube, searchText = searchText).body()?.let {
                for (song in it.songs) songs.add(Song.buildOnline(song))
                _songList.value = songs
            }
        } catch (e: Exception) {
            printLogcat(e)
        }
    }
}