package com.example.muzik.data_model.standard_model

import android.net.Uri
import com.example.muzik.data_model.api_model.response.AlbumResponse
import com.example.muzik.music_service.LocalMusicRepository

class Album (
    val name: String? = null,
    val imageURI: Uri? = null,
    val albumID: Long? = -1,
    val artistID: Long? = -1,
    val artistName: String? = ""
) : Model {
    val listSongId: MutableList<Long> = ArrayList()

    companion object {
        fun buildLocal(albumID: Long, album: String, albumArtUri: Uri, artistID: Long): Album {
            return Album(
                albumID = albumID,
                name = album,
                imageURI = albumArtUri,
                artistID = artistID
            )
        }

        fun buildOnline(albumResponse: AlbumResponse): Album {
            return Album(
                albumID = albumResponse.albumID,
                name = albumResponse.name,
                imageURI = Uri.parse(albumResponse.imageURL),
                artistID = albumResponse.artistID,
                artistName = albumResponse.artistName
            )
        }
    }

    fun getArtist(): Artist {
        return artistID?.let { LocalMusicRepository.getArtist(it) } as Artist
    }

    fun getSongs(): List<Song> {
        val listSong: MutableList<Song> = ArrayList()
        for (songId: Long in listSongId) {
            LocalMusicRepository.getSong(songId)?.let { listSong.add(it) }
        }
        return listSong
    }

    fun requireAlbumID(): Long {
        checkNotNull(albumID) {
            ("Album $this must have non-null album ID")
        }
        return albumID
    }
}