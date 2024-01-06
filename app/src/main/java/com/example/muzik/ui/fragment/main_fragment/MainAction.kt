package com.example.muzik.ui.fragment.main_fragment

import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.ui.Action

interface MainAction : Action {
    fun goToArtistFragment(artistID: Long? = null, artist: Artist?)
    fun goToPlaylistFragment(playlistID: Long? = null, playlist: Playlist?)
    fun goToAlbumFragment(albumID: Long? = null, album: Album?)

    fun goToCreatePlaylistActivity()

    fun addSongToPlaylist(playlistID: Long)
    fun addSongToStreamList(songID: String)
}