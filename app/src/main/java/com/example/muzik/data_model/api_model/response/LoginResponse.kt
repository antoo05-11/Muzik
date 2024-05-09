package com.example.muzik.data_model.api_model.response

data class LoginResponse(
    val userID: Long,
    val username: String,
    val name: String,
    val email: String?,
    val phoneNumber: String?,
    val accessToken: String
)

