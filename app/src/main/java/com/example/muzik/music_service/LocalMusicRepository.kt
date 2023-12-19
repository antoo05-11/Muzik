package com.example.muzik.music_service

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Song


object LocalMusicRepository {
    private val mapSong: MutableMap<Long, Song> = HashMap()
    private val mapAlbum: MutableMap<Long, Album> = HashMap()
    private val mapArtist: MutableMap<Long, Artist> = HashMap()

    fun getSong(id: Long): Song? {
        return mapSong[id]
    }

    fun getArtist(id: Long): Artist? {
        return mapArtist[id]
    }

    fun getAlbum(id: Long): Album? {
        return mapAlbum[id]
    }

    fun getSongs(): List<Song> {
        return mapSong.values.toList()
    }

    fun getArtists(): List<Artist> {
        return mapArtist.values.toList()
    }

    fun getAlbums(): List<Album> {
        return mapAlbum.values.toList()
    }

    fun fetchSong(context: Context) {
        val songLibraryUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,

            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ARTIST,

            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST
        )

        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC"

        context.contentResolver.query(songLibraryUri, projection, null, null, sortOrder)
            .use { cursor ->

                val songIdColumn: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val displayNameColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val albumIdColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val albumColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumArtistColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)
                val artistIdColumn: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
                val artistColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

                while (cursor.moveToNext()) {
                    val songId: Long = cursor.getLong(songIdColumn)
                    val displayName: String? = cursor.getString(displayNameColumn)
                    val duration: Int = cursor.getInt(durationColumn)
                    val size: Int = cursor.getInt(sizeColumn)
                    val albumId: Long = cursor.getLong(albumIdColumn)
                    val album: String? = cursor.getString(albumColumn)
                    val albumArtist: String? = cursor.getString(albumArtistColumn)
                    val artistId: Long = cursor.getLong(artistIdColumn)
                    val artist: String? = cursor.getString(artistColumn)

                    if (displayName == null) continue

                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songId
                    )
                    val albumArtUri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )
                    val song = Song.buildLocal(
                        songId, uri,
                        displayName ?: "",
                        duration, size, albumId, artistId
                    )
                    mapSong[songId] = song
                    if (mapAlbum.containsKey(albumId)) {
                        mapAlbum[albumId]!!.listSongId.add(songId)
                    } else {
                        val newAlbum = Album.buildLocal(albumId, album ?: "", albumArtUri, artistId)
                        newAlbum.listSongId.add(songId)
                        mapAlbum[albumId] = newAlbum
                    }
                    if (mapArtist.containsKey(artistId)) {
                        mapArtist[artistId]!!.listSongId.add(songId)
                    } else {
                        val newArtist = Artist.buildLocal(artistId, artist ?: "")
                        newArtist.listSongId.add(songId)
                        mapArtist[artistId] = newArtist
                    }
                }
            }
    }
}