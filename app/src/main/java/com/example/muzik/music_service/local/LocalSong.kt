package com.example.muzik.music_service.local

import android.net.Uri
import com.example.muzik.music_service.model.Album
import com.example.muzik.music_service.model.Artist
import com.example.muzik.music_service.model.Song

class LocalSong(
    val songID: Long,
    uri: Uri,
    name: String,
    duration: Int,
    val size: Int,
    private val albumId: Long,
    private val artistId: Long): Song(uri, name, duration){
    override fun getArtist(): Artist {
        return LocalMusicRepository.getArtist(artistId) ?: LocalArtist(-1, "Unknown")
    }

    override fun getAlbum(): Album {
        return LocalMusicRepository.getAlbum(albumId) ?: LocalAlbum(-1, "Unknown", Uri.parse("default"), -1)
    }

    override fun getImg(): Uri {
        return LocalMusicRepository.getAlbum(albumId)?.albumArtUri ?: Uri.parse("")
    }
}