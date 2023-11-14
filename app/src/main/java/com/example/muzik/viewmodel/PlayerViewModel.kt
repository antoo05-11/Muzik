package com.example.muzik.viewmodel

import android.net.Uri
import android.os.Handler
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.muzik.response_model.Song

class PlayerViewModel : ViewModel() {

    //live data
    var exoPlayerMutableLiveData: MutableLiveData<ExoPlayer> = MutableLiveData()
        private set

    var playingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var songMutableLiveData: MutableLiveData<Song> = MutableLiveData()
        private set

    var currentTimeMutableLiveData: MutableLiveData<Int> = MutableLiveData()
        private set

    var isSelectedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
        private set

    init {
        updateCurrentProgress()
        isSelectedMutableLiveData.value = false
    }

    class OnSeekBarChangeListener(private val playerViewModel: PlayerViewModel) :
        SeekBar.OnSeekBarChangeListener {
        var progressValue = 0
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            progressValue = seekBar!!.progress
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            seekBar!!.progress = progressValue
            playerViewModel.currentTimeMutableLiveData.value = progressValue
            playerViewModel.player.seekTo(seekBar.progress.toLong())
        }

    }

    val player get() = exoPlayerMutableLiveData.value!!

    fun setPlaying(isPlaying: Boolean) {
        playingMutableLiveData.value = isPlaying
    }

    fun setSong(song: Song) {
        isSelectedMutableLiveData.value = true
        songMutableLiveData.value = song
    }

    fun addListener() {
        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    setPlaying(isPlaying)
                    updateCurrentProgress()
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updateCurrentProgress()
                }
            }
        )
    }

    fun skipNextSong() {
        if (player.hasNextMediaItem()) {
            player.seekToNext()
        }
    }

    fun skipPreSong() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPrevious()
        }
    }

    fun playPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun setMedia(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun stop() {
        player.stop()
    }

    private fun updateCurrentProgress() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (player.isPlaying) {
                    currentTimeMutableLiveData.value = player.currentPosition.toInt()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }
}