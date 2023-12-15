package com.example.muzik.ui.chart_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Chart.SongWithView
import com.example.muzik.response_model.Song
import com.example.muzik.ui.main_activity.MainActivity

class ChartViewModel : ViewModel() {
    private val _chartSongsList: MutableLiveData<List<Song>> = MutableLiveData()
    val chartSongsList = _chartSongsList as LiveData<List<Song>>

    private val _chart: MutableLiveData<List<SongWithView>> = MutableLiveData()
    val chart = _chart as LiveData<List<SongWithView>>

    suspend fun fetchData() {
        try {
            _chartSongsList.value = MainActivity.muzikAPI.getAllSongs().body()
        }
        catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }

    }

    suspend fun fetchChartViewData() {
        try {
            _chart.value = MainActivity.muzikAPI.getSongCharts().body()
        }
        catch (e: Throwable) {
            Log.e("NETWORK_ERROR", "Network error!")
        }
    }
}