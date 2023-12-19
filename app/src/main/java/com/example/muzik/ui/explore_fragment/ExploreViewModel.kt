package com.example.muzik.ui.explore_fragment

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
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
            try {
                val playlistList = mutableListOf<Playlist>()
                muzikAPI.getTopPlaylists().body()?.let {
                    for (i in it) playlistList.add(Playlist.buildOnline(i))
                }
                _topPlaylistsList.value = playlistList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }

        lifecycleCoroutineScope.launch {
            try {
                val songList = mutableListOf<Song>()
                muzikAPI.getYourTopSongs().body()?.let {
                    for (i in it) {
                        songList.add(Song.buildOnline(i))
                    }
                    _topSongsList.value = songList
                }
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }

        lifecycleCoroutineScope.launch {
            try {
                val playlistList = mutableListOf<Playlist>()
                muzikAPI.getTopPlaylists().body()?.let {
                    for (i in it) playlistList.add(Playlist.buildOnline(i))
                }
                _listenAgainPlaylistsList.value = playlistList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }

        lifecycleCoroutineScope.launch {
            try {
                val artistList = mutableListOf<Artist>()
                muzikAPI.getYourArtists().body()?.let {
                    for (i in it) artistList.add(Artist.buildOnline(i))
                }
                _yourArtistsList.value = artistList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }

        lifecycleCoroutineScope.launch {
            try {
                val albumList = mutableListOf<Album>()
                muzikAPI.getRecentAlbums().body()?.let {
                    for (i in it) {
                        albumList.add(Album.buildOnline(i))
                    }
                }
                _recentAlbumsList.value = albumList
            } catch (e: Throwable) {
                Log.e("NETWORK_ERROR", "Network error!")
            }
        }
    }
}