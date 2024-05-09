package com.example.muzik.data_model.api_model.response

import android.net.Uri
import com.example.muzik.data_model.standard_model.Song

data class AlbumResponse(
    val albumID: Long = 0,
    val name: String? = null,
    val albumArtURI: Uri? = null,
    val imageURL: String? = null,
    val artistID: Long = 0,
    val albumArtist: String? = null,
    val artistName: String? = null,
    val songs: List<Song>? = null,
)