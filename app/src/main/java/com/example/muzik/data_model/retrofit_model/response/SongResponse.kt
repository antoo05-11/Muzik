package com.example.muzik.data_model.retrofit_model.response

data class SongResponse(
    val songID: String,
    val name: String,
    val imageURL: String,
    val artistName: String?,
    val artistID: Long?,
    val songURL: String? = "",
    val views: Long?,
    val duration: Int = 0
)