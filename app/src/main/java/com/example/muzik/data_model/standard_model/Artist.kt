package com.example.muzik.data_model.standard_model

import android.net.Uri
import com.example.muzik.data_model.retrofit_model.response.ArtistResponse
import com.example.muzik.music_service.LocalMusicRepository

class Artist private constructor(
    val name: String? = "",
    val artistID: Long? = -1,
    val imageURI: Uri? = null
) : Model {
    val listSongId: MutableList<Long> = ArrayList()

    constructor(isNewSample: Boolean) : this()

    companion object {
        fun buildOnline(artistResponse: ArtistResponse): Artist {
            return Artist(
                name = artistResponse.name,
                artistID = artistResponse.artistID,
                imageURI = Uri.parse(artistResponse.imageURL)
            )
        }

        fun buildLocal(artistID: Long, name: String): Artist {
            return Artist(artistID = artistID, name = name)
        }
    }

    fun getSongs(): List<Song> {
        val listSong: MutableList<Song> = ArrayList()
        for (songId: Long in listSongId) {
            listSong.add(LocalMusicRepository.getSong(songId) as Song)
        }
        return listSong
    }
}