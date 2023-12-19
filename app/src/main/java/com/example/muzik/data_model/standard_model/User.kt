package com.example.muzik.data_model.standard_model

import java.util.Date

class User(
    val userID: Long?,
    var name: String?,
    val email: String?,
    val phoneNumber: Int?,
    val dateOfBirth: Date?,
) : Model {
    val password: String? = null
}