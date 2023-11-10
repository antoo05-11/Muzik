package com.example.muzik.model

import android.net.Uri

class Song(val id: Long, val uri: Uri, val name: String, val duration: Int, val size: Int, val album: String?, val artist: String?, val artistID: Long?) {

}