package com.example.muzik.ui.player_view_fragment

import android.net.Uri
import android.os.Handler
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity.Companion.musicService

class PlayerViewModel : ViewModel() {

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

    var repeatModeMutableLiveData: MutableLiveData<Int> = MutableLiveData()
        private set

    var shuffleModeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
        private set



    init {
        isSelectedMutableLiveData.value = false
    }

    private var songHashMap: HashMap<String, Song> = HashMap()

    class OnSeekBarChangeListener(private val playerViewModel: PlayerViewModel) :
        SeekBar.OnSeekBarChangeListener {
        private var progressValue = 0
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

        musicService!!.setSong(song)
    }

    fun setListSong(listSong: List<Song>, index: Int = 0) {
        isSelectedMutableLiveData.value = true
        songMutableLiveData.value = listSong[index]
        musicService!!.setListSong(listSong, index)
    }

    fun addListener() {
        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    setPlaying(isPlaying)
                    updateCurrentProgress()
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    if(musicService!!.curSong != null) {
                        songMutableLiveData.value = musicService!!.curSong
                    }
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

    fun switchRepeatMode() {
        musicService!!.switchRepeatMode()
        repeatModeMutableLiveData.value = musicService!!.repeatMode
    }

    fun switchShuffleMode() {
        musicService!!.switchShuffleMode()
        shuffleModeMutableLiveData.value = musicService!!.shuffleMode
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