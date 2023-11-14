package com.example.muzik.response_model

data class Artist(
    val artistID: Long,
    val name: String?,
    val songs: MutableList<Song>,
    val imageURL: String?
)