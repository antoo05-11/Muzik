package com.example.muzik.music_service.local

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log

object LocalMusicRepository {
    private val mapSong: MutableMap<Long, LocalSong> = HashMap()
    private val mapAlbum: MutableMap<Long, LocalAlbum> = HashMap()
    private val mapArtist: MutableMap<Long, LocalArtist> = HashMap()

    fun getSong(id: Long): LocalSong? {
        return mapSong[id]
    }

    fun getArtist(id: Long): LocalArtist? {
        return mapArtist[id]
    }

    fun getAlbum(id: Long): LocalAlbum? {
        return mapAlbum[id]
    }

    fun getSongs(): List<LocalSong> {
        return mapSong.values.toList()
    }

    fun getArtists(): List<LocalArtist> {
        return mapArtist.values.toList()
    }

    fun getAlbums(): List<LocalAlbum> {
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
                    val albumArtis: String? = cursor.getString(albumArtistColumn)
                    val artistId: Long = cursor.getLong(artistIdColumn)
                    val artist: String? = cursor.getString(artistColumn)

                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songId
                    )
                    val albumArtUri = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    )
                    val song = LocalSong(
                            songId,
                            uri,
                            displayName ?: "",
                            duration,
                            size,
                            albumId,
                            artistId
                        )
                    mapSong[songId] = song
                    if (mapAlbum.containsKey(albumId)) {
                        mapAlbum[albumId]!!.listSongId.add(songId)
                    } else {
                        val newAlbum = LocalAlbum(albumId, album ?: "", albumArtUri, artistId)
                        newAlbum.listSongId.add(songId)
                        mapAlbum[albumId] = newAlbum
                    }
                    if (mapArtist.containsKey(artistId)) {
                        mapArtist[artistId]!!.listSongId.add(songId)
                    } else {
                        val newArtist = LocalArtist(artistId, artist ?: "")
                        newArtist.listSongId.add(songId)
                        mapArtist[artistId] = newArtist
                    }
                }
            }
        //log-test
        var i = 1
        for(song in getSongs()) {
            Log.e("DanhPB", (i.toString() + ": " + song.name))
            i++
        }
    }
}