package com.example.muzik.ui.bottom_sheet_dialog.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class SongsOptionBottomSheetViewModel : ViewModel() {
    private val _song = MutableLiveData<Song>(null)
    val song = _song as LiveData<Song>
    suspend fun fetchYoutubeSong(songID: String) {
        try {
            muzikAPI.getSong(songID = songID, youtube = true).body()?.let {
                _song.value =  Song.buildOnline(it)
            }
        } catch (e: Exception) {
            printLogcat(e)
        }
    }
}