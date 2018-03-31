package com.alorma.rac1.service

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.FLAG_NO_CLEAR
import android.support.v4.content.ContextCompat
import com.alorma.rac1.R


class LiveRadioService : Service() {
    companion object {
        private const val LIVE_URL = "http://rac1.radiocat.net:8090/"

        private const val NOTIFICATION_ID = 1

        private const val AUDIO_CHANNEL = "audio"
        private const val AUDIO_CHANNEL_NAME = "Escoltar la radio"
        private const val CHANNEL_LIVE = "live"
        private const val CHANNEL_LIVE_NAME = "Radio en directe"
    }

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, Uri.parse(LIVE_URL)).apply {
            val attrs = AudioAttributes.Builder().build()
            setAudioAttributes(attrs)
        }
    }

    override fun onBind(intent: Intent?): IBinder = LiveBinder(this)

    fun play() {
        showNotification()
        mediaPlayer.start()
    }

    private fun showNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioGroup = NotificationChannelGroup(AUDIO_CHANNEL, AUDIO_CHANNEL_NAME)
            nm.createNotificationChannelGroup(audioGroup)

            val channel = NotificationChannel(CHANNEL_LIVE, CHANNEL_LIVE_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                group = AUDIO_CHANNEL
                setShowBadge(false)
                lightColor = ContextCompat.getColor(this@LiveRadioService, R.color.colorPrimary)
            }

            nm.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_LIVE).apply {
            setSmallIcon(R.drawable.ic_play)
            setContentTitle("Reproduint")
            priority = NotificationCompat.PRIORITY_HIGH
            setChannelId(CHANNEL_LIVE)

        }.build().apply {
            flags = FLAG_NO_CLEAR
        }

        nm.notify(NOTIFICATION_ID, notification)
    }

    fun pause() {
        mediaPlayer.pause()
    }

    class LiveBinder(private val liveRadioService: LiveRadioService) : Binder() {
        fun getService(): LiveRadioService = liveRadioService
    }
}