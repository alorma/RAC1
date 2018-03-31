package com.alorma.rac1.service

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class LocalPlayback(private val context: Context) : Playback {

    companion object {

        const val LIVE_URL = "http://rac1.radiocat.net:8090/"

        // The volume we set the media player to when we lose audio focus, but are
        // allowed to reduce the volume instead of stopping playback.
        const val VOLUME_DUCK = 0.2f
        // The volume we set the media player when we have audio focus.
        const val VOLUME_NORMAL = 1.0f

        // we don't have audio focus, and can't duck (play at a low volume)
        private const val AUDIO_NO_FOCUS_NO_DUCK = 0
        // we don't have focus, but can duck (play at a low volume)
        private const val AUDIO_NO_FOCUS_CAN_DUCK = 1
        // we have full audio focus
        private const val AUDIO_FOCUSED = 2
    }

    private val mContext: Context
    private val mWifiLock: WifiManager.WifiLock
    private var mPlayOnFocusGain: Boolean = false
    private var mCallback: Playback.Callback? = null

    private var mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
    private val mAudioManager: AudioManager

    private val mExoPlayer: SimpleExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context)
                , DefaultTrackSelector(), DefaultLoadControl()).apply {
            addListener(mEventListener)

            val attributes = AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build()
            audioAttributes = attributes
        }
    }
    private val mEventListener = ExoPlayerEventListener()

    // Whether to return STATE_NONE or STATE_STOPPED when mExoPlayer is null;
    private var mExoPlayerNullIsStopped = false

    override// Nothing to do (mExoPlayer holds its own state).
    var state: Int
        get() {
            return when (mExoPlayer.playbackState) {
                Player.STATE_IDLE -> PlaybackStateCompat.STATE_PAUSED
                Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
                Player.STATE_READY -> if (mExoPlayer.playWhenReady) {
                    PlaybackStateCompat.STATE_PLAYING
                } else {
                    PlaybackStateCompat.STATE_PAUSED
                }
                Player.STATE_ENDED -> PlaybackStateCompat.STATE_PAUSED
                else -> PlaybackStateCompat.STATE_NONE
            }
        }
        set(state) {}

    override val isConnected: Boolean
        get() = true

    override val isPlaying: Boolean
        get() = mPlayOnFocusGain || mExoPlayer.playWhenReady

    private val mOnAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> mCurrentAudioFocusState = AUDIO_FOCUSED
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Lost audio focus, but will gain it back (shortly), so note whether
                // playback should resume
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
                mPlayOnFocusGain = mExoPlayer.playWhenReady
            }
            AudioManager.AUDIOFOCUS_LOSS ->
                // Lost audio focus, probably "permanently"
                mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }

        configurePlayerState()
    }


    init {
        val applicationContext = context.applicationContext
        this.mContext = applicationContext

        this.mAudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        this.mWifiLock = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "rac1")
    }

    override fun start() {
        // Nothing to do
    }

    override fun stop(notifyListeners: Boolean) {
        giveUpAudioFocus()
        releaseResources(true)
    }

    override fun updateLastKnownStreamPosition() {
        // Nothing to do. Position maintained by ExoPlayer.
    }

    override fun play() {
        mPlayOnFocusGain = true
        tryToGetAudioFocus()
        releaseResources(false)
        val dataSourceFactory = DefaultDataSourceFactory(mContext, "rac1", null)
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ExtractorMediaSource(Uri.parse(LIVE_URL), dataSourceFactory, extractorsFactory, null, null)
        mExoPlayer.prepare(mediaSource)
        mWifiLock.acquire()
        configurePlayerState()
    }

    override fun pause() {
        mExoPlayer.playWhenReady = false
        releaseResources(false)
    }

    override fun setCallback(callback: Playback.Callback) {
        this.mCallback = callback
    }

    private fun tryToGetAudioFocus() {
        val result = mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_FOCUSED
        } else {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    private fun giveUpAudioFocus() {
        val state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build())
        } else {
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener)
        }

        if (state == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    /**
     * Reconfigures the player according to audio focus settings and starts/restarts it. This method
     * starts/restarts the ExoPlayer instance respecting the current audio focus state. So if we
     * have focus, it will play normally; if we don't have focus, it will either leave the player
     * paused or set it to a low volume, depending on what is permitted by the current focus
     * settings.
     */
    private fun configurePlayerState() {
        if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
            // We don't have audio focus and can't duck, so we have to pause
            pause()
        } else {

            if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
                // We're permitted to play, but only if we 'duck', ie: play softly
                mExoPlayer.volume = VOLUME_DUCK
            } else {
                mExoPlayer.volume = VOLUME_NORMAL
            }

            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                mExoPlayer.playWhenReady = true
                mPlayOnFocusGain = false
            }
        }
    }

    /**
     * Releases resources used by the service for playback, which is mostly just the WiFi lock for
     * local playback. If requested, the ExoPlayer instance is also released.
     *
     * @param releasePlayer Indicates whether the player should also be released
     */
    private fun releaseResources(releasePlayer: Boolean) {
        // Stops and releases player (if requested and available).
        if (releasePlayer) {
            mExoPlayer.release()
            mExoPlayer.removeListener(mEventListener)
            mExoPlayerNullIsStopped = true
            mPlayOnFocusGain = false
        }

        if (mWifiLock.isHeld) {
            mWifiLock.release()
        }
    }

    private inner class ExoPlayerEventListener : Player.EventListener {
        override fun onTimelineChanged(timeline: Timeline, manifest: Any) {

        }

        override fun onTracksChanged(
                trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

        }

        override fun onLoadingChanged(isLoading: Boolean) {

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE, Player.STATE_BUFFERING, Player.STATE_READY -> if (mCallback != null) {
                    mCallback?.onPlaybackStatusChanged(state)
                }
                Player.STATE_ENDED ->
                    mCallback?.onCompletion()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val what: String? = when (error.type) {
                ExoPlaybackException.TYPE_SOURCE -> error.sourceException.message
                ExoPlaybackException.TYPE_RENDERER -> error.rendererException.message
                ExoPlaybackException.TYPE_UNEXPECTED -> error.unexpectedException.message
                else -> "Unknown: $error"
            }

            what?.let {
                mCallback?.onError("ExoPlayer error $it")
            }
        }

        override fun onPositionDiscontinuity() {
            // Nothing to do.
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            // Nothing to do.
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            // Nothing to do.
        }
    }
}