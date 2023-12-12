package me.danhpb.danhpbexoplayer.exoplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.example.muzik.MuzikApplication
import com.example.muzik.R
import me.danhpb.danhpbexoplayer.exoplayer.model.Song

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

    private lateinit var exoPlayer: ExoPlayer
    private var curSong: Song? = null
    private var curProgress: Int = 0


    inner class MyBinder: Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private val binder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        initExoPlayer()
    }

    private fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.addListener(object : Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                sendNotification()
                sendUpdate()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                sendNotification()
                sendUpdate()
            }
        })
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
        builder.setContentText(curSong?.getArtist()?.name)
//        builder.setLargeIcon(bitmap)
        val notification = builder.build()
        startForeground(ID, notification)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(this, MusicReceiver::class.java)
        intent.putExtra(ACTION, action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun playPause() {
        if(exoPlayer.isPlaying) {
            exoPlayer.pause()
        }
        else {
            exoPlayer.play()
        }
    }

    fun setSong(song: Song) {
        curSong = song
        val mediaItem = MediaItem.fromUri(song.uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun skipNextSong() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
        }
    }

    fun skipPreSong() {
        if(exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
        }
    }

    fun release() {
        exoPlayer.release()
    }

    fun stop() {
        exoPlayer.stop()
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