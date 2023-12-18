package com.example.muzik.data_model.retrofit_model.response

import com.example.muzik.response_model.User

data class LoginResponse(
    val error: Boolean?,
    val message: String?,
    val accessToken: String?,
    val user: User?
)

