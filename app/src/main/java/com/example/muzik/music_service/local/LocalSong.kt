package me.danhpb.danhpbexoplayer.exoplayer.local

import android.net.Uri
import me.danhpb.danhpbexoplayer.exoplayer.model.Album
import me.danhpb.danhpbexoplayer.exoplayer.model.Artist
import me.danhpb.danhpbexoplayer.exoplayer.model.Song

class LocalSong(
    val songID: Long,
    uri: Uri,
    name: String,
    duration: Int,
    val size: Int,
    val albumId: Long,
    val artistId: Long): Song(uri, name, duration){
    override fun getArtist(): Artist {
        return LocalMusicRepository.getArtist(artistId) ?: LocalArtist(-1, "Unknow")
    }

    override fun getAlbum(): Album {
        return LocalMusicRepository.getAlbum(albumId) ?: LocalAlbum(-1, "Unknow", Uri.parse("default"), -1)
    }

    override fun getImg(): Uri {
        return LocalMusicRepository.getAlbum(albumId)?.albumArtUri ?: Uri.parse("")
    }
}