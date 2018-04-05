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
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.ui.MainActivity
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LiveRadioService : MediaBrowserServiceCompat(), LivePlaybackManager.PlaybackServiceCallback {

    companion object {
        const val CMD = "CMD"
        const val CMD_STOP = "CMD_STOP"

        const val LIVE_URL = "http://rac1.radiocat.net:8090/"
    }

    @Inject
    lateinit var programsRepository: ProgramsRepository

    @Inject
    lateinit var mediaNotificationManager: MediaNotificationManager

    @Inject
    lateinit var playbackManager: LivePlaybackManager

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val mSession: MediaSessionCompat by lazy { MediaSessionCompat(this, "MusicService") }

    private lateinit var program: ProgramItem
    private var session: SessionDto? = null

    override fun onCreate() {
        super.onCreate()
        component inject this

        loadCurrentProgram()
        connectPlaybackPublisher()

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

    private fun loadCurrentProgram() {
        disposable += Flowable.zip(
                Flowable.interval(5, TimeUnit.SECONDS),
                programsRepository.getPrograms().flattenAsFlowable { it },
                BiFunction<Long, ProgramItem, ProgramItem> { _, program -> program }
        ).subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    livePublisher.onNext(it)
                }, {}, {
                    livePublisher.onComplete()
                })
    }

    private fun connectPlaybackPublisher() {
        disposable += playbackPublisher.subscribeOnIO().observeOnUI().subscribe({
            onStreamPlayback(it)
        }, {

        })
    }

    private fun onStreamPlayback(it: StreamPlayback) {
        when (it) {
            Stop -> playbackManager.handleStopRequest()
            is Play -> {
                this@LiveRadioService.program = it.programItem
                this@LiveRadioService.session = null
                when (it) {
                    is Live -> {
                        playbackManager.handlePlayRequest(LIVE_URL)
                    }
                    is Podcast -> {
                        this@LiveRadioService.session = it.sessionDto
                        playbackManager.handlePlayRequest(it.sessionDto.path)
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.hasExtra(CMD)) {
            if (intent.getStringExtra(CMD) == CMD_STOP) {
                playbackPublisher.onNext(Stop)
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
        disposable.clear()
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
        val notification = mediaNotificationManager.show(mSession.sessionToken, program, session)
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
