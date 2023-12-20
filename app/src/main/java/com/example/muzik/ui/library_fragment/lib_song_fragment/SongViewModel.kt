package com.example.muzik.ui.library_fragment.lib_song_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Song

class SongViewModel : ViewModel() {
    var songsMutableLiveData: MutableLiveData<List<Song>> = MutableLiveData()
    private var listSong: MutableList<Song> = ArrayList()
    private var albumsLiveData: MutableLiveData<MutableMap<Long, Album>> = MutableLiveData()
    private var mapAlbum: MutableMap<Long, Album> = HashMap()
    val artistsLiveData: MutableLiveData<MutableMap<Long, Artist>> = MutableLiveData()
    private var mapArtist: MutableMap<Long, Artist> = HashMap()

    init {
        songsMutableLiveData.value = listSong
        albumsLiveData.value = mapAlbum
        artistsLiveData.value = mapArtist
    }
}