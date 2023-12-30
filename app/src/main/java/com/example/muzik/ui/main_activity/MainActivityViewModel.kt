package com.example.muzik.ui.main_activity

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.utils.printLogcat
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _playlists: MutableLiveData<List<Playlist>> = MutableLiveData(mutableListOf())
    val playlists = _playlists as LiveData<List<Playlist>>

    private lateinit var lifecycleCoroutineScope: LifecycleCoroutineScope

    fun setLifecycleCoroutineScope(lifecycleCoroutineScope: LifecycleCoroutineScope): MainActivityViewModel {
        this.lifecycleCoroutineScope = lifecycleCoroutineScope
        return this
    }

    fun fetchData() {
        lifecycleCoroutineScope.launch {
            try {
                val playlists = mutableListOf<Playlist>()
                MainActivity.muzikAPI.getTopPlaylists().body()?.let {
                    for (i in it) playlists.add(Playlist.buildOnline(i))
                }
                _playlists.value = playlists
            } catch (e: Exception) {
                printLogcat(e)
            }
        }
    }
}