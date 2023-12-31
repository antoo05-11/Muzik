package com.example.muzik.ui.bottom_sheet_dialog.playlists

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.retrofit_model.request.PlaylistSongRequest
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat
import kotlinx.coroutines.launch

class PlaylistBottomSheetViewModel : ViewModel() {

    private lateinit var lifecycleCoroutineScope: LifecycleCoroutineScope

    fun setLifecycleCoroutineScope(lifecycleCoroutineScope: LifecycleCoroutineScope): PlaylistBottomSheetViewModel {
        this.lifecycleCoroutineScope = lifecycleCoroutineScope
        return this
    }

    fun addSongToPlaylist(songID: String, playlistID: Long) {
        lifecycleCoroutineScope.launch {
            try {
                muzikAPI.addSongToPlaylist(
                    playlistID = playlistID,
                    playlistSongRequest = PlaylistSongRequest(songID)
                ).body()?.let {

                }
            } catch (e: Exception) {
                printLogcat(e)
            }
        }
    }
}