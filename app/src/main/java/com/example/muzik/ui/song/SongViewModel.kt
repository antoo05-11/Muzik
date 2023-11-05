package com.example.muzik.ui.song

import androidx.lifecycle.ViewModel
import com.example.muzik.api_controller.response_model.Song

class SongViewModel: ViewModel() {

    private lateinit var song: Song
    val name: String
        get() = song.name
    val artistName: String
        get() = song.artistName
    val imageURL: String
        get() = song.imageURL
}