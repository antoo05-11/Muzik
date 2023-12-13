package com.example.muzik.music_service.local

import android.net.Uri
import com.example.muzik.music_service.model.Album
import com.example.muzik.music_service.model.Artist
import com.example.muzik.music_service.model.Song

class LocalAlbum(
    val albumId: Long,
    name: String,
    val albumArtUri: Uri,
    val artistId: Long
): Album(name) {

    val listSongId: MutableList<Long> = ArrayList()

    override fun getArtist(): Artist {
        return LocalMusicRepository.getArtist(artistId) as Artist
    }

    override fun getSongs(): List<Song> {
        val listSong: MutableList<Song> = ArrayList()
        for(songId: Long in listSongId) {
            listSong.add(LocalMusicRepository.getSong(songId) as Song)
        }
        return listSong
    }

    override fun getImg(): Uri {
        return albumArtUri
    }
}