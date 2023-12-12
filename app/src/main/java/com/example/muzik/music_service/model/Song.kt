package me.danhpb.danhpbexoplayer.exoplayer.model

import android.net.Uri
import java.io.Serializable

abstract class Song(
    val uri: Uri,
    val name: String,
    val duration: Int,
) : Serializable {
    abstract fun getArtist(): Artist?
    abstract fun getAlbum(): Album?
    abstract fun getImg(): Uri?
}