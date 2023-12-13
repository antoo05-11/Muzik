package com.example.muzik.music_service.model

import android.net.Uri
import java.io.Serializable

abstract class Song(
    open var uri: Uri?,
    open var name: String?,
    open var duration: Int?,
) : Serializable {
    abstract fun getArtist(): Artist?
    abstract fun getAlbum(): Album?
    abstract fun getImg(): Uri?
    open fun getArtistName(): String? {
        getArtist()?.let { return it.name }
        return null
    }
}