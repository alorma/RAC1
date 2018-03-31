package com.alorma.rac1.service

interface Playback {

    var state: Int

    val isPlaying: Boolean

    fun play()

    fun stop()

    interface Callback {
        fun onPlaybackStatusChanged(state: Int)
        fun onError(error: String)
    }

    fun setCallback(callback: Callback)
}