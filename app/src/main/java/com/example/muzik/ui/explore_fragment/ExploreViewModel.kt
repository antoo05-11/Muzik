package com.example.muzik.ui.explore_fragment

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.Playlist
import com.example.muzik.response_model.Song
import com.example.muzik.ui.main_activity.MainActivity
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private var _topPlaylistsList = MutableLiveData<List<Playlist>>()
    val topPlaylistsList = _topPlaylistsList as LiveData<List<Playlist>>

    private var _topSongsList = MutableLiveData<List<Song>>()
    val topSongList = _topSongsList as LiveData<List<Song>>

    private var _listenAgainPlaylistsList = MutableLiveData<List<Playlist>>()
    val listenAgainPlaylistsList = _listenAgainPlaylistsList as LiveData<List<Playlist>>

    private var _recentAlbumsList = MutableLiveData<List<Album>>()
    val recentAlbumsList = _recentAlbumsList as LiveData<List<Album>>

    private var _yourArtistsList = MutableLiveData<List<Artist>>()
    val yourArtistsList = _yourArtistsList as LiveData<List<Artist>>

    private var fetched: MutableLiveData<Boolean> = MutableLiveData()

    init {
        fetched.value = false
    }

    fun fetchData(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        lifecycleCoroutineScope.launch {
            val topPlaylistsRes = MainActivity.muzikAPI.getTopPlaylists()
            _topPlaylistsList.value = topPlaylistsRes.body()
        }
        lifecycleCoroutineScope.launch {
            val topSongsRes = MainActivity.muzikAPI.getYourTopSongs()
            _topSongsList.value = topSongsRes.body()
        }
        lifecycleCoroutineScope.launch {
            val listenAgainPlaylistsRes = MainActivity.muzikAPI.getTopPlaylists()
            _listenAgainPlaylistsList.value = listenAgainPlaylistsRes.body()
        }
        lifecycleCoroutineScope.launch {
            val yourArtistsListRes = MainActivity.muzikAPI.getYourArtists()
            _yourArtistsList.value = yourArtistsListRes.body()
        }
        lifecycleCoroutineScope.launch {
            val recentAlbumsListRes = MainActivity.muzikAPI.getRecentAlbums()
            _recentAlbumsList.value = recentAlbumsListRes.body()
        }
    }
}