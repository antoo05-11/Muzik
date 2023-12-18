package com.example.muzik.data_model.retrofit_model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String
)
