package com.example.muzik.music_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.muzik.ui.main_activity.MainActivity

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getIntExtra(MusicService.ACTION, 0)) {
            (MusicService.ACTION_PLAY_PAUSE) -> MainActivity.musicService?.playPause()
            (MusicService.ACTION_SKIP_NEXT) -> MainActivity.musicService?.skipNextSong()
            (MusicService.ACTION_SKIP_PRE) -> MainActivity.musicService?.skipPreSong()
        }
    }
}