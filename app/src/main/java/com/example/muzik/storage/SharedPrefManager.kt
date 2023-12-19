package com.example.muzik.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.example.muzik.data_model.standard_model.User
import java.util.Date

class SharedPrefManager private constructor(private val mCtx: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_pref"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE_NUMBER = "user_phoneNumber"
        private const val KEY_USER_DATE_OF_BIRTH = "user_dateOfBirth"


        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

//    fun decodeJwtPayload(accessToken: String): Map<String, Any> {
//        val jwt = JWT(accessToken)
//
////        val issuer = jwt.issuer
////        val isAdmin = jwt.getClaim("isAdmin").asString()
//
//        val isExpired = jwt.isExpired(10)
//        val jwtInfo = mutableMapOf<String, Any>()
//
//        jwtInfo["isExpired"] = isExpired
//        return jwtInfo
//    }

    val isAccessTokenExpired: Boolean
        get() {
            val accessToken: String? = this.accessToken
            return accessToken != null && JWT(accessToken).isExpired(10)
        }

    fun saveAccessToken(accessToken: String) {
        val sharedPreferences: SharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun saveUser(user: User) {
        val sharedPreferences: SharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        user.userID?.let { editor.putLong(KEY_USER_ID, it) }
        editor.putString(KEY_USER_EMAIL, user.email)
        editor.putString(KEY_USERNAME, user.name)
        user.dateOfBirth?.let { editor.putLong(KEY_USER_DATE_OF_BIRTH, it.time) }
        user.phoneNumber?.let { editor.putString(KEY_USER_PHONE_NUMBER, it) }
        editor.apply()
    }

    private val accessToken: String?
        get() {
            val sharedPreferences: SharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        }

    val user: User
        get() {
            val sharedPreferences: SharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val userId = sharedPreferences.getLong(KEY_USER_ID, -1)
            val name = sharedPreferences.getString(KEY_USERNAME, null)
            val email = sharedPreferences.getString(KEY_USER_EMAIL, null)
            val dateOfBirthLong = sharedPreferences.getLong(KEY_USER_DATE_OF_BIRTH, -1)
            val dateOfBirth = if (dateOfBirthLong != -1L) Date(dateOfBirthLong) else null
            val userPhone = sharedPreferences.getString(KEY_USER_PHONE_NUMBER, null)

            return User(userId, name, email, userPhone, dateOfBirth)
        }

    val isLoggedIn: Boolean
        get() {
            val accessToken: String? = this.accessToken
            return !accessToken.isNullOrEmpty()
        }

    fun clear() {
        val sharedPreferences: SharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
