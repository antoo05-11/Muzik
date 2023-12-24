package com.example.muzik.ui.library_fragment.lib_artist_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.utils.printLogcat

class ArtistViewModel : ViewModel() {
    private val _artists = MutableLiveData<List<Artist>>(mutableListOf())
    val artists = _artists as LiveData<List<Artist>>

    suspend fun fetchData() {
        muzikAPI.getYourArtists().body()?.let {
            try {
                val artists = mutableListOf<Artist>()
                for (i in it) artists.add(Artist.buildOnline(i))
                _artists.value = artists
            } catch (e: Exception) {
                printLogcat(e)
            }
        }
    }
}