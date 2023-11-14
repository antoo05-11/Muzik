package com.example.muzik.response_model

import java.util.Date

data class Playlist(
    val playlistID: Long,
    val name: String?,
    val dateAdded: Date?
)
