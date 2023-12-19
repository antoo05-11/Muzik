package com.example.muzik.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.muzik.R
import com.example.muzik.data_model.standard_model.User

class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val img_avatar: ImageView = findViewById(R.id.ivAvatar)
        val tv_name: TextView = findViewById(R.id.tv_name_profile)
        val tv_username: TextView = findViewById(R.id.tv_username_profile)
        val tv_email: TextView = findViewById(R.id.tv_email_profile)
        val tv_phone: TextView = findViewById(R.id.tv_phone_profile)
        val edit_btn: Button = findViewById(R.id.btn_edit_profile)
        val btn_back_home: ImageView = findViewById(R.id.iv_back_home)

        // Lấy thông tin người dùng từ SharedPrefManager
        val sharedPrefManager = SharedPrefManager.getInstance(this)
        val user: User? = sharedPrefManager.user

        // Hiển thị thông tin người dùng nếu tồn tại
        user?.let {
            tv_name.text = it.name
            tv_username.text = it.name
            tv_email.text = it.email
            tv_phone.text = it.phoneNumber.toString()
        }

        edit_btn.setOnClickListener {  }


        btn_back_home.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}