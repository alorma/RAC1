package com.alorma.rac1.service

import android.app.PendingIntent
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
        //mMediaNotificationManager = MediaNotificationManager(this)
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
        playbackManager.handleStopRequest(null)
        // mMediaNotificationManager!!.stopNotification()
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
        mSession.isActive = true

        // The service needs to continue running even after the bound client (usually a
        // MediaController) disconnects, otherwise the music playback will stop.
        // Calling startService(Intent) will keep the service running until it is explicitly killed.
        startService(Intent(applicationContext, LiveRadioService::class.java))
    }


    /**
     * Callback method called from PlaybackManager whenever the music stops playing.
     */
    override fun onPlaybackStop() {
        stopForeground(true)
    }

    override fun onNotificationRequired() {
        //mMediaNotificationManager!!.startNotification()
    }

    override fun onPlaybackStateUpdated(newState: PlaybackStateCompat) {
        mSession.setPlaybackState(newState)
    }
}
