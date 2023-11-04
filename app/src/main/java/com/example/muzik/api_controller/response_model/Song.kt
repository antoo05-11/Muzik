package com.example.muzik.api_controller.response_model

data class Song(
    val songID: Int,
    val imageURL: String,
    val artistID: String,
    val artistName: String,
    val name: String
)