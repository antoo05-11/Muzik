package com.example.muzik.music_service.local

import android.net.Uri
import com.example.muzik.music_service.model.Album
import com.example.muzik.music_service.model.Artist
import com.example.muzik.music_service.model.Song

class LocalArtist(
    val artistId: Long,
    name: String,
): Artist(name) {
    val listSongId: MutableList<Long> = ArrayList()
    override fun getSongs(): List<Song> {
        val listSong: MutableList<Song> = ArrayList()
        for(songId: Long in listSongId) {
            listSong.add(LocalMusicRepository.getSong(songId) as Song)
        }
        return listSong
    }

    override fun getAlbum(): List<Album> {
        TODO("Not yet implemented")
    }

    override fun getImg(): Uri {
        TODO("Not yet implemented")
    }

}