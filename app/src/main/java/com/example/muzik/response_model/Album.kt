package com.example.muzik.response_model

import android.net.Uri

data class Album(
    val id: Long,
    val name: String?,
    val artist: String?,
    val art: Uri,
    val songs: MutableList<Song>
) {
}