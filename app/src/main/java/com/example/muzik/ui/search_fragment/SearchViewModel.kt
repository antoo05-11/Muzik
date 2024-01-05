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

    private val _suggestionList: MutableLiveData<List<String>> = MutableLiveData()
    val suggestionList = _suggestionList as LiveData<List<String>>

    interface Callback {
        fun onSuccess()
        fun onError(error: Exception)
    }

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

    suspend fun fetchSearchSuggestions(searchText: String = "") {
        if (searchText.isNotEmpty())
            try {
                muzikAPI.getSuggestions(q = searchText).body()?.let {
                    _suggestionList.value = it
                }
            } catch (e: Exception) {
                printLogcat(e)
            }
        else _suggestionList.value = mutableListOf()
    }
}