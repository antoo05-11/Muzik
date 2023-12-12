package me.danhpb.danhpbexoplayer.exoplayer.local

import android.net.Uri
import me.danhpb.danhpbexoplayer.exoplayer.model.Album
import me.danhpb.danhpbexoplayer.exoplayer.model.Artist
import me.danhpb.danhpbexoplayer.exoplayer.model.Song

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