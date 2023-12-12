package me.danhpb.danhpbexoplayer.exoplayer.model

import android.net.Uri

abstract class Album(
    val name: String
    ) {
    abstract fun getArtist(): Artist?
    abstract fun getSongs(): List<Song>
    abstract fun getImg(): Uri?
}