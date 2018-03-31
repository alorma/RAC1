package com.alorma.rac1.service

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import javax.inject.Inject

class LivePlaybackManager @Inject constructor(private val playback: Playback) : Playback.Callback {

    lateinit var serviceCallback: PlaybackServiceCallback
    val mediaSessionCallback = MediaSessionCallback()

    private val availableActions: Long
        get() {
            var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE
            actions = if (playback.isPlaying) {
                actions or PlaybackStateCompat.ACTION_PAUSE
            } else {
                actions or PlaybackStateCompat.ACTION_PLAY
            }
            return actions
        }

    init {
        this.playback.setCallback(this)
    }

    /**
     * Handle a request to play music
     */
    fun handlePlayRequest() {
        serviceCallback.onPlaybackStart()
        playback.play()
    }

    /**
     * Handle a request to stop music
     *
     * @param withError Error message in case the stop has an unexpected cause. The error
     * message will be set in the PlaybackState and will be visible to
     * MediaController clients.
     */
    fun handleStopRequest(withError: String? = null) {
        playback.stop()
        serviceCallback.onPlaybackStop()
        updatePlaybackState(withError)
    }

    fun updatePlaybackState(error: String? = null) {
        val stateBuilder = PlaybackStateCompat.Builder()
                .setActions(availableActions)

        val state = playback.state

        stateBuilder.setState(state, 0, 1.0f, SystemClock.elapsedRealtime())

        serviceCallback.onPlaybackStateUpdated(stateBuilder.build())
    }

    override fun onPlaybackStatusChanged(state: Int) {
        updatePlaybackState()
    }

    override fun onError(error: String) {
        updatePlaybackState(error)
    }

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            handlePlayRequest()
        }

        override fun onSkipToQueueItem(queueId: Long) {

        }

        override fun onSeekTo(position: Long) {

        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {

        }

        override fun onPause() {
        }

        override fun onStop() {
            handleStopRequest()
        }

        override fun onSkipToNext() {

        }

        override fun onSkipToPrevious() {

        }

        override fun onCustomAction(action: String, extras: Bundle?) {

        }

        override fun onPlayFromSearch(query: String?, extras: Bundle?) {

        }
    }

    interface PlaybackServiceCallback {
        fun onPlaybackStart()

        fun onPlaybackStop()

        fun onPlaybackStateUpdated(newState: PlaybackStateCompat)
    }
}