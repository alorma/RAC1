package com.alorma.rac1.ui

import android.content.ComponentName
import android.content.Intent
import android.os.RemoteException
import android.support.v4.app.Fragment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.alorma.rac1.service.LiveRadioService

class PlayerFragment : Fragment() {

    lateinit var playerCallback: PlayerCallback

    private val mediaBrowserCompat: MediaBrowserCompat by lazy {
        MediaBrowserCompat(context, ComponentName(context, LiveRadioService::class.java),
                mediaBrowserCompatConnectionCallback, null)
    }
    private var isPlaying: Boolean = false

    private val mediaCallback: MediaControllerCompat.Callback by lazy {
        object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                super.onPlaybackStateChanged(state)

                onStateChanged(state)
            }
        }
    }

    private val mediaBrowserCompatConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                activity?.let { act ->
                    MediaControllerCompat(act, mediaBrowserCompat.sessionToken).apply {
                        onStateChanged(playbackState)
                        registerCallback(mediaCallback)
                        MediaControllerCompat.setMediaController(act, this)
                    }
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

        override fun onConnectionSuspended() {

            // We were connected, but no longer :-(
        }

        override fun onConnectionFailed() {
            // The attempt to connect failed completely.
            // Check the ComponentName!
        }
    }

    fun togglePlay() {
        activity?.let { act ->
            val mediaController = MediaControllerCompat.getMediaController(act)
            if (isPlaying) {
                mediaController?.transportControls?.stop()
            } else {
                val intent = Intent(act, LiveRadioService::class.java)
                act.startForegroundService(intent)
                mediaController?.transportControls?.play()
                playerCallback.onPlayPlayback()
            }
            isPlaying = isPlaying.not()
        }
    }


    private fun onStateChanged(state: PlaybackStateCompat) {
        when (state.state) {
            PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {
                isPlaying = false
                playerCallback.onStopPlayback()
            }
            PlaybackStateCompat.STATE_ERROR -> {
            }
            else -> {
                playerCallback.onPlayPlayback()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        activity?.let { act ->
            val playbackState = MediaControllerCompat.getMediaController(act)?.playbackState
            if (playbackState != null) {
                onStateChanged(playbackState)
            }

            mediaBrowserCompat.connect()
        }
    }

    override fun onStop() {
        activity?.let { act ->
            MediaControllerCompat.getMediaController(act)
                    ?.unregisterCallback(mediaCallback)
            mediaBrowserCompat.disconnect()
        }
        super.onStop()
    }

    interface PlayerCallback {
        fun onPlayPlayback()
        fun onStopPlayback()
    }

}