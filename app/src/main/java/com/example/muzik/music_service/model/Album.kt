package com.example.muzik.music_service.model

import android.net.Uri

abstract class Album(
    val name: String
    ) {
    abstract fun getArtist(): Artist?
    abstract fun getSongs(): List<Song>
    abstract fun getImg(): Uri?
}