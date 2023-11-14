package com.example.muzik.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.MainActivity
import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.Song
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    var songsMutableLiveData: MutableLiveData<List<Song>> = MutableLiveData()
    private var listSong: MutableList<Song> = ArrayList()
    private var albumsLiveData: MutableLiveData<MutableMap<Long, Album>> = MutableLiveData()
    private var mapAlbum: MutableMap<Long, Album> = HashMap()
    val artistsLiveData: MutableLiveData<MutableMap<Long, Artist>> = MutableLiveData()
    private var mapArtist: MutableMap<Long, Artist> = HashMap()

    init {
        songsMutableLiveData.value = listSong
        albumsLiveData.value = mapAlbum
        artistsLiveData.value = mapArtist
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchSong(context: Context) {
        val songLibraryUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        //projection
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

        //sort order

        //sort order
        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC"

        context.contentResolver.query(songLibraryUri, projection, null, null, sortOrder)
            .use { cursor ->

                //cache the cursor indices
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

                //getting the values
                while (cursor.moveToNext()) {
                    //get values of columns for a give audio file
                    val songId: Long = cursor.getLong(songIdColumn)
                    val displayName: String = cursor.getString(displayNameColumn)
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
                    val song =
                        Song(
                            songId, uri, displayName, duration,
                            size, album, artist, artistId,
                            "", 0
                        )
                    listSong.add(song)
                    if (mapAlbum.containsKey(albumId)) {
                        mapAlbum[albumId]!!.songs.add(song)
                    } else {
                        val newAlbum = Album(albumId, album, albumArtis, albumArtUri, ArrayList())
                        newAlbum.songs.add(song)
                        mapAlbum[newAlbum.id] = newAlbum
                    }

                    if (mapArtist.containsKey(artistId)) {
                        mapArtist[artistId]!!.songs.add(song)
                    } else {
                        val newArtist = Artist(artistId, artist, ArrayList(), "")
                        newArtist.songs.add(song)
                        mapArtist[artistId] = newArtist
                    }

                }
            }
        GlobalScope.launch {
            val result = MainActivity.muzikAPI.getAllSongs()
            result.body()?.forEach { song ->
                listSong.add(song)
                val newArtist =
                    Artist(song.artistID!!, song.artistName)
                newArtist.songs.add(song)
                mapArtist[newArtist.artistID] = newArtist
            }
        }
    }
}