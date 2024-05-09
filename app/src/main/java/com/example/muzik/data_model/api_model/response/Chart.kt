package com.example.muzik.data_model.api_model.response

import com.example.muzik.data_model.standard_model.Song
import java.util.Date

class Chart {
    data class SongWithView(
        val song: Song,
        var songViews: List<SongView>
    )

    data class SongView(
        val songID: Long = 0,
        val date: Date,
        val views: Long = 0,
    )
}