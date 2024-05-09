package com.example.muzik.ui.bottom_sheet_dialog.playlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.api_model.request.PlaylistSongRequest
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class PlaylistBottomSheetViewModel : ViewModel() {

    val addSongToPlaylistSuccessfully = MutableLiveData(StatusCode.NONE)

    enum class StatusCode {
        SUCCESS, ERROR, NONE
    }

    suspend fun addSongToPlaylist(songID: String, playlistID: Long, accessToken: String) {
            try {
                muzikAPI.addSongToPlaylist(
                    playlistID = playlistID,
                    playlistSongRequest = PlaylistSongRequest(songID),
                    authHeader = "Bearer $accessToken"
                ).body()?.let {
                    addSongToPlaylistSuccessfully.value = StatusCode.SUCCESS
                }
            } catch (e: Exception) {
                printLogcat(e)
            }
    }
}