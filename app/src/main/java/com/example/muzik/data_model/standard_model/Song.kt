package com.example.muzik.data_model.standard_model

import android.net.Uri
import com.example.muzik.data_model.retrofit_model.response.SongResponse
import com.example.muzik.music_service.LocalMusicRepository

class Song(
    val songID: String? = null,
    val name: String? = null,
    var artistName: String? = null,
    var songURI: Uri? = null,
    val views: Long? = -1,
    val imageURI: Uri? = null,
    val artistID: Long? = -1,
    var duration: Int = -1,
    val size: Int? = -1,
    val albumID: Long? = -1
) : Model {

    companion object {
        fun buildOnline(songResponse: SongResponse): Song {
            var songURI: Uri? = null
            songResponse.songURL?.let {
                //songURI = Uri.parse(it.replace("http:/", "https://"))
                songURI = Uri.parse(it)
            }
            return Song(
                songID = songResponse.songID,
                name = songResponse.name,
                artistName = songResponse.artistName,
                songURI = songURI,
                views = songResponse.views,
                imageURI = Uri.parse(songResponse.imageURL),
                artistID = songResponse.artistID,
                duration = songResponse.duration
            )
        }

        fun buildLocal(
            songId: Long,
            uri: Uri,
            displayName: String,
            duration: Int,
            size: Int,
            albumID: Long,
            artistID: Long
        ): Song {
            val artist = LocalMusicRepository.getArtist(artistID)
                ?: Artist.buildLocal(-1, "Unknown")
            return Song(
                songID = songId.toString(),
                name = displayName,
                songURI = uri,
                artistID = artistID,
                duration = duration,
                size = size,
                albumID = albumID,
                artistName = artist.name
            )
        }
    }

    fun getAlbum(): Album {
        return albumID?.let { LocalMusicRepository.getAlbum(it) } ?: Album.buildLocal(
            -1,
            "Unknown",
            Uri.parse("default"),
            -1
        )
    }

    fun requireSongID(): String {
        checkNotNull(songID) {
            ("Song $this must have non-null songID")
        }
        return songID
    }

    fun requireArtistID(): Long {
        checkNotNull(artistID) {
            ("Song $this must have non-null artist ID")
        }
        return artistID
    }
}