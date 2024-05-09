package com.example.muzik.ui.activity.create_playlist_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.data_model.api_model.request.CreatePlaylistRequest
import com.example.muzik.databinding.ActivityCreatePlaylistBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.activity.login_activity.LoginActivity
import kotlinx.coroutines.launch

class CreatePlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePlaylistBinding
    private lateinit var viewModel: CreatePlaylistActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CreatePlaylistActivityViewModel::class.java]

        binding.createNewPlaylistButton.setOnClickListener {
            val type = if (binding.privateSwitchButton.isChecked) "PRIVATE" else "PUBLIC"
            val requestBody =
                CreatePlaylistRequest(binding.playlistNameInputText.text.toString(), type)
            val accessToken: String? = SharedPrefManager.getInstance(applicationContext).accessToken
            if (accessToken == null) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            accessToken?.let {
                lifecycleScope.launch {
                    viewModel.createPlaylist(requestBody, it)
                    finish()
                }
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}