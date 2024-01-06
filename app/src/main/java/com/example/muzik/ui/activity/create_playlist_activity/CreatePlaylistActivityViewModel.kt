package com.example.muzik.ui.activity.create_playlist_activity

import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.retrofit_model.request.CreatePlaylistRequest
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.muzikAPI

class CreatePlaylistActivityViewModel : ViewModel() {
    suspend fun createPlaylist(createPlaylistRequest: CreatePlaylistRequest, accessToken: String) {
        muzikAPI.createPlaylist(
            createPlaylistRequest = createPlaylistRequest,
            authHeader = "Bearer $accessToken"
        )
    }
}