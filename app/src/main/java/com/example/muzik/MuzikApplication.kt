package com.example.muzik

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MuzikApplication: Application() {
    companion object {
        const val CHANNEL_ID = "Muzik"
        private const val CHANNEL_NAME = "Muzik"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChanel()
    }

    private fun createNotificationChanel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}