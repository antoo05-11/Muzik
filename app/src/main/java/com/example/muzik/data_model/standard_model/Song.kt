package com.example.muzik.data_model.standard_model

import android.net.Uri
import com.example.muzik.data_model.retrofit_model.response.SongResponse
import com.example.muzik.music_service.LocalMusicRepository

class Song(
    val songID: Long = -1,
    val name: String? = "",
    var artistName: String? = null,
    val songURI: Uri = Uri.parse(""),
    val views: Long? = -1,
    val imageURI: Uri? = null,
    val artistID: Long? = -1,
    val duration: Int = -1,
    val size: Int? = -1,
    val albumID: Long? = -1
) : Model {

    fun getAlbum(): Album {
        return albumID?.let { LocalMusicRepository.getAlbum(it) } ?: Album.buildLocal(
            -1,
            "Unknown",
            Uri.parse("default"),
            -1
        )
    }

    companion object {
        fun buildOnline(songResponse: SongResponse): Song {
            return Song(
                songID = songResponse.songID,
                name = songResponse.name,
                artistName = songResponse.artistName,
                songURI = Uri.parse(songResponse.songURL),
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
                songID = songId,
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
}