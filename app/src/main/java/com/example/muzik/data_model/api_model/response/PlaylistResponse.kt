package com.example.muzik.data_model.api_model.response

import java.util.Date

data class PlaylistResponse(
    val playlistID: Long,
    val type: String,
    val userID: Long,
    val name: String,
    val dateAdded: Date,
    val imageURL: String
)