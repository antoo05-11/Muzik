package com.example.muzik.music_service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.example.muzik.MuzikApplication
import com.example.muzik.R
import com.example.muzik.data_model.standard_model.Song
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class MusicService : Service() {
    companion object {
        private const val ID = 1
        const val ACTION = "MUSIC_SERVICE_ACTION"
        const val ACTION_PLAY_PAUSE = 0
        const val ACTION_SKIP_NEXT = 1
        const val ACTION_SKIP_PRE = 2
        const val INTENT_FILTER = "MUSIC_SERVICE_INTENT"
        const val KEY_CUR_SONG = "CUR_SONG"
        const val KEY_CUR_PROGRESS = "CUR_PROGRESS"
        const val KEY_IS_PLAYING = "IS_PLAYING"
    }

    lateinit var exoPlayer: ExoPlayer
        private set
    var curSong: Song? = null
    private var curProgress: Int = 0

    var repeatMode: Int = Player.REPEAT_MODE_OFF

    var shuffleMode: Boolean = false

    var songHashMap: HashMap<String, Song> = HashMap()

    inner class MyBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private val binder = MyBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        initExoPlayer()
    }

    private fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.addListener(object : Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                curSong = songHashMap[mediaItem?.mediaId]
                sendNotification()
                sendUpdate()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                sendNotification()
                sendUpdate()
            }
        })
        exoPlayer.repeatMode = repeatMode
        exoPlayer.shuffleModeEnabled = shuffleMode
        updateCurrentProgress()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        exoPlayer.release()
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, MuzikApplication.CHANNEL_ID)
        builder.foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        builder.priority = NotificationCompat.PRIORITY_HIGH

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        builder.setSmallIcon(R.drawable.baseline_music_note_24)
        builder.addAction(
            R.drawable.baseline_skip_previous_24,
            "Previous",
            getPendingIntent(this, ACTION_SKIP_PRE)
        )
        if (exoPlayer.isPlaying) {
            builder.addAction(
                R.drawable.baseline_pause_24,
                "Play",
                getPendingIntent(this, ACTION_PLAY_PAUSE)
            )
        } else {
            builder.addAction(
                R.drawable.baseline_play_arrow_24,
                "Play",
                getPendingIntent(this, ACTION_PLAY_PAUSE)
            )
        }
        builder.addAction(
            R.drawable.baseline_skip_next_24,
            "Next",
            getPendingIntent(this, ACTION_SKIP_NEXT)
        )
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
        )
        builder.setContentTitle(curSong?.name)
        builder.setContentText(curSong?.artistName)

        curSong?.let {
            Picasso.get().load(it.imageURI)
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        builder.setLargeIcon(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }
                })
        }

        val notification = builder.build()
        startForeground(ID, notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(context, MusicReceiver::class.java)
        intent.putExtra(ACTION, action)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(
                context.applicationContext,
                action,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun playPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }

    fun switchRepeatMode() {
        repeatMode += 1
        if (repeatMode > 2) {
            repeatMode = 0
        }
        Log.d("DanhPB: ", repeatMode.toString())
        exoPlayer.repeatMode = repeatMode
    }

    fun switchShuffleMode() {
        shuffleMode = !shuffleMode
        exoPlayer.shuffleModeEnabled = shuffleMode
    }


    fun skipNextSong() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
        }
    }

    fun skipPreSong() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
        }
    }

    fun release() {
        exoPlayer.release()
    }

    fun stop() {
        exoPlayer.stop()
    }


    fun setListSong(listSong: List<Song>, index: Int = 0) {
        exoPlayer.clearMediaItems()
        for (song: Song in listSong) {
            song.requireSongID().let {
                val mediaItem = MediaItem.Builder().setUri(song.songURI).setMediaId(it).build()
                songHashMap[it] = song
                exoPlayer.addMediaItem(mediaItem)
            }
        }
        exoPlayer.seekTo(index, 0)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun addSongToPlayingList(song: Song) {
        val mediaItem =
            MediaItem.Builder().setUri(song.songURI).setMediaId(song.requireSongID()).build()
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
    }


    fun setSong(song: Song) {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        song.songID?.let {
            val mediaItem = MediaItem.Builder().setUri(song.songURI).setMediaId(it).build()
            songHashMap[it] = song
            exoPlayer.addMediaItem(mediaItem)
        }
        exoPlayer.prepare()
        exoPlayer.play()
    }

    private fun updateCurrentProgress() {
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (exoPlayer.isPlaying) {
                    curProgress = exoPlayer.currentPosition.toInt()
                    sendUpdate()
                }
                handler.postDelayed(this, 1000)
            }
        })
    }


    private fun sendUpdate() {
        val intent = Intent(INTENT_FILTER)
        val bundle = Bundle()
        bundle.putSerializable(KEY_CUR_SONG, curSong)
        bundle.putInt(KEY_CUR_PROGRESS, curProgress)
        bundle.putBoolean(KEY_IS_PLAYING, exoPlayer.isPlaying)
        intent.putExtras(bundle)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.sendBroadcast(intent)
    }
}