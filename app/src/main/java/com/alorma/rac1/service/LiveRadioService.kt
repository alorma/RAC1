package com.alorma.rac1.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.ui.MainActivity
import javax.inject.Inject

class LiveRadioService : MediaBrowserServiceCompat(), LivePlaybackManager.PlaybackServiceCallback {

    companion object {
        const val CMD = "CMD"
        const val CMD_STOP = "CMD_STOP"
    }

    @Inject
    lateinit var mediaNotificationManager: MediaNotificationManager

    @Inject
    lateinit var playbackManager: LivePlaybackManager

    private val mSession: MediaSessionCompat by lazy { MediaSessionCompat(this, "MusicService") }

    override fun onCreate() {
        super.onCreate()

        component inject this

        playbackManager.serviceCallback = this

        // Start a new MediaSession
        sessionToken = mSession.sessionToken
        mSession.setCallback(playbackManager.mediaSessionCallback)
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(applicationContext, 99 /*request code*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mSession.setSessionActivity(pi)
        mSession.setExtras(Bundle())

        playbackManager.updatePlaybackState(null)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.hasExtra(CMD)) {
            if (intent.getStringExtra(CMD) == CMD_STOP) {
                playbackManager.handleStopRequest()
            }
        }

        return Service.START_STICKY_COMPATIBILITY
    }

    /*
     * Handle case when user swipes the app away from the recents apps list by
     * stopping the service (and any ongoing playback).
     */
    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    /**
     * (non-Javadoc)
     * @see android.app.Service.onDestroy
     */
    override fun onDestroy() {
        playbackManager.handleStopRequest()
        mediaNotificationManager.destroy()
        mSession.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int,
                           rootHints: Bundle?): MediaBrowserServiceCompat.BrowserRoot? {
        return MediaBrowserServiceCompat.BrowserRoot("", null)
    }

    override fun onLoadChildren(parentMediaId: String,
                                result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>) {
        result.sendResult(null)
    }

    /**
     * Callback method called from PlaybackManager whenever the music is about to play.
     */
    override fun onPlaybackStart() {
        val notification = mediaNotificationManager.show(mSession.sessionToken)
        mSession.isActive = true

        startForeground(MediaNotificationManager.ID_LIVE, notification)

        // The service needs to continue running even after the bound client (usually a
        // MediaController) disconnects, otherwise the music playback will stop.
        // Calling startService(Intent) will keep the service running until it is explicitly killed.
        startService(Intent(applicationContext, LiveRadioService::class.java))
    }


    /**
     * Callback method called from PlaybackManager whenever the music stops playing.
     */
    override fun onPlaybackStop() {
        mediaNotificationManager.hide()
        stopForeground(true)
    }

    override fun onPlaybackStateUpdated(newState: PlaybackStateCompat) {
        mSession.setPlaybackState(newState)
    }
}
