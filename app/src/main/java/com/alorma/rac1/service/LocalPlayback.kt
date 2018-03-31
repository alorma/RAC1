package com.alorma.rac1.service

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
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
        val renders = object : DefaultRenderersFactory(context) {

            /*
            override fun createRenderers(eventHandler: Handler?, videoRendererEventListener: VideoRendererEventListener?, audioRendererEventListener: AudioRendererEventListener?, textRendererOutput: TextRenderer.Output?, metadataRendererOutput: MetadataRenderer.Output?): Array<Renderer> {
                val items = arrayListOf<Renderer>()
                buildAudioRenderers(context, null, buildAudioProcessors(), eventHandler, audioRendererEventListener, EXTENSION_RENDERER_MODE_OFF, items)
                return items.toTypedArray()
            }
            */
        }

        ExoPlayerFactory.newSimpleInstance(renders, DefaultTrackSelector(), DefaultLoadControl()).apply {

            val attributes = AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build()
            audioAttributes = attributes
        }
    }

    override
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

    override val isPlaying: Boolean
        get() = mPlayOnFocusGain || mExoPlayer.playWhenReady

    init {
        val applicationContext = context.applicationContext
        this.mContext = applicationContext

        this.mAudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        this.mWifiLock = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "rac1")
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

    override fun stop() {
        mExoPlayer.playWhenReady = false
        giveUpAudioFocus()
        releaseResources(false)
    }

    override fun setCallback(callback: Playback.Callback) {
        this.mCallback = callback
    }

    private fun tryToGetAudioFocus() {
        val result = mAudioManager.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build())

        mCurrentAudioFocusState = if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            AUDIO_FOCUSED
        } else {
            AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    private fun giveUpAudioFocus() {
        val state = mAudioManager.abandonAudioFocusRequest(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build())

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
        if (mCurrentAudioFocusState != AUDIO_NO_FOCUS_NO_DUCK) {
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
            mPlayOnFocusGain = false
        }

        if (mWifiLock.isHeld) {
            mWifiLock.release()
        }
    }
}