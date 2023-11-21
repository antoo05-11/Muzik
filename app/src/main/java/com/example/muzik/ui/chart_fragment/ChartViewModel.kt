package com.example.muzik.ui.chart_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Song
import com.example.muzik.ui.main_activity.MainActivity

class ChartViewModel : ViewModel() {
    private val _chartSongsList: MutableLiveData<List<Song>> = MutableLiveData()
    val chartSongsList = _chartSongsList as LiveData<List<Song>>

    suspend fun fetchData() {
        _chartSongsList.value = MainActivity.muzikAPI.getAllSongs().body()
    }
}