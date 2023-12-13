package com.example.muzik.music_service.model

import android.net.Uri

abstract class Artist(
    val name: String
) {
    abstract fun getSongs(): List<Song>
    abstract fun getAlbum(): List<Album>
    abstract fun getImg(): Uri?
}