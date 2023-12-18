package com.example.muzik.data_model.retrofit_model.response

data class SongResponse(
    val songID: Long,
    val name: String,
    val imageURL: String,
    val artistName: String?,
    val artistID: Long?,
    val songURL: String,
    val views: Long?,
    val duration: Int
)