package com.example.muzik.music_service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.muzik.ui.main_activity.MainActivity

class MusicReceiver: BroadcastReceiver() {
    private var isServiceConnected = false
    private var musicService: MusicService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            musicService = binder.getService()
            isServiceConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            MainActivity.musicService = null
            isServiceConnected = false
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val bindServiceIntent = Intent(context, MusicService::class.java)
        context?.bindService(bindServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        when(intent?.getIntExtra(MusicService.ACTION, 0)) {
            (MusicService.ACTION_PLAY_PAUSE) -> musicService?.playPause()
            (MusicService.ACTION_SKIP_NEXT) -> musicService?.skipNextSong()
            (MusicService.ACTION_SKIP_PRE) -> musicService?.skipPreSong()
        }
        context?.unbindService(serviceConnection)
        musicService = null
        isServiceConnected = false
    }
}