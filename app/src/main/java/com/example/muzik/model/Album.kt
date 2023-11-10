package com.example.muzik.model

import android.net.Uri

class Album(val id: Long, val name: String?, val artist: String?, val art: Uri, val songs: MutableList<Song>) {
}