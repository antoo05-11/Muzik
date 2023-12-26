package com.example.muzik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.SongOptionsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class SongOptionsBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(SongOptionsBottomSheetBinding::bind)
    private lateinit var song: Song

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.song_options_bottom_sheet, container, false)
    }

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        song.name.apply {
            binding.songNameTextView.text = this.toString()
        }

        song.artistName.apply {
            binding.artistNameTextView.text = this.toString()
        }

        Picasso.get().load(song.imageURI).into(binding.artistImageView)

//        val cache = SimpleCache(File("/storage/self/primary/Download"), NoOpCacheEvictor())
//        val cacheDataSourceFactory: CacheDataSource.Factory = CacheDataSource.Factory()
//            .setCache(cache)
//            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
//
//        val hlsDownloader = HlsDownloader(
//            MediaItem.Builder()
//                .setUri(song.songURI)
//                .setStreamKeys(
//                    listOf(
//                        StreamKey(HlsMultivariantPlaylist.GROUP_INDEX_VARIANT, 0)
//                    )
//                )
//                .build(),
//            cacheDataSourceFactory
//        )

//        Thread {
//            hlsDownloader.download { _, _, percentDownloaded ->
//                printLogcat(
//                    percentDownloaded.toString()
//                )
//            }
//            val mediaItem: MediaItem = MediaItem.Builder().build()
//            val mediaSource =
//                HlsMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)
//
//            musicService?.exoPlayer?.setMediaSource(mediaSource)
//
//            musicService?.exoPlayer?.prepare()
//
//            musicService?.exoPlayer?.playWhenReady = true
//        }.start()


    }

    fun setSongInfo(song: Song) {
        this.song = song
    }

    companion object {
        const val TAG = "SongOptionsBottomSheet"
    }
}