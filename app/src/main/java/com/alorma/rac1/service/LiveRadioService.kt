package com.alorma.rac1.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder

class LiveRadioService : Service() {
    companion object {
        private const val LIVE_URL = "http://rac1.radiocat.net:8090/"
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
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    class LiveBinder(private val liveRadioService: LiveRadioService) : Binder() {
        fun getService(): LiveRadioService = liveRadioService
    }
}