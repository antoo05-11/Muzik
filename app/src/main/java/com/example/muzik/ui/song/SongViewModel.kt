package com.example.muzik.ui.song

import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Song

class SongViewModel: ViewModel() {

    private lateinit var song: Song
    val name: String
        get() = song.name!!
    val artistName: String
        get() = song.artist.toString()
    val imageURL: String?
        get() = song.imageURL
}