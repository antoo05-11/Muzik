package com.example.muzik.ui.bottom_sheet_dialog.stream_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.api_model.request.StreamListRequest
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class StreamListBottomSheetViewModel : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>(mutableListOf())
    val songs = _songs as LiveData<List<Song>>

    suspend fun fetchAllSong(songIDs: List<Long>) {
        try {
            muzikAPI.getSongsWithSongIDs(StreamListRequest(songIDs = songIDs)).body()?.let {
                val songs = mutableListOf<Song>()
                for (i in it) songs.add(Song.buildOnline(i))
                _songs.value = songs
            }
        } catch (e: Exception) {
            printLogcat(e)
            throw e
        }
    }
}