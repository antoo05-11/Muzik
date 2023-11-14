package com.example.muzik.response_model

import android.net.Uri

data class Song(
    val songID: Long? = -1,
    val uri: Uri,
    val name: String? = "",
    val duration: Int? = 0,
    val size: Int? = 0,
    val album: String? = "",
    val artist: String?,
    val artistID: Long?,
    val artistName: String?,
    val imageURL: String?,
    val albumID: Int? = 0,
    val songURL: String?
)