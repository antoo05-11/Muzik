package com.example.muzik.data_model.standard_model

import com.example.muzik.data_model.retrofit_model.response.LoginResponse
import java.util.Date

class User(
    val userID: Long?,
    var name: String?,
    val email: String?,
    val phoneNumber: String?,
    val dateOfBirth: Date?,
) : Model {
    val password: String? = null

    companion object {
        fun build(loginResponse: LoginResponse): User {
            return User(
                userID = loginResponse.userID,
                name = loginResponse.name,
                email = loginResponse.email,
                phoneNumber = loginResponse.phoneNumber,
                dateOfBirth = null
            )
        }
    }
}