package com.example.muzik.ui.main_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muzik.data_model.standard_model.User
import com.example.muzik.databinding.ActivityProfileBinding
import com.example.muzik.storage.SharedPrefManager

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefManager = SharedPrefManager.getInstance(this)
        val user: User = sharedPrefManager.user

        user.let {
            binding.nameTextView.text = it.name
            binding.usernameTextView.text = it.name
        }

        binding.ivBackHome.setOnClickListener {
            finish()
        }
    }
}