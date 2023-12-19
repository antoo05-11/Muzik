package com.example.muzik.data_model.standard_model

import android.net.Uri

data class Comment(
    val avatarUri: Uri? = null,
    val username: String,
    val comment: String
)