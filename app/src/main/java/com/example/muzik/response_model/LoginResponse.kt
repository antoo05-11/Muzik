package com.example.muzik.response_model

data class LoginResponse(
    val error: Boolean,
    val message: String?,
    val accessToken: String?,
    val user: User?
)

