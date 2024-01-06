package com.example.muzik.ui.fragment.chart_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.retrofit_model.response.Chart.SongWithView
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.muzikAPI

class ChartViewModel : ViewModel() {
    private val _chartSongsList: MutableLiveData<List<Song>> = MutableLiveData()
    val chartSongsList = _chartSongsList as LiveData<List<Song>>

    private val _chart: MutableLiveData<List<SongWithView>> = MutableLiveData()
    val chart = _chart as LiveData<List<SongWithView>>

    suspend fun fetchData() {
        try {
            val songList = mutableListOf<Song>()
            muzikAPI.getAllSongs().body()?.let {
                for (i in it) {
                    songList.add(Song.buildOnline(i))
                }
                _chartSongsList.value = songList
            }
        } catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }

    }

    suspend fun fetchChartViewData() {
        try {
            _chart.value = muzikAPI.getSongCharts().body()
        } catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }
    }
}