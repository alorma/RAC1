package com.alorma.rac1.ui

import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.app.Fragment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.service.LiveRadioService
import com.alorma.rac1.service.StreamPlayback
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PlayConnectionFragment : Fragment() {

    @Inject
    lateinit var programsRepository: ProgramsRepository

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    private val mediaBrowserCompat: MediaBrowserCompat by lazy {
        MediaBrowserCompat(context, ComponentName(context, LiveRadioService::class.java),
                mediaBrowserCompatConnectionCallback, null)
    }

    var isPlaying: Boolean = false

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    private val mediaCallback: MediaControllerCompat.Callback by lazy { object : MediaControllerCompat.Callback() {} }

    private val mediaBrowserCompatConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                activity?.let { act ->
                    MediaControllerCompat(act, mediaBrowserCompat.sessionToken).apply {
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

    private var liveProgram: ProgramItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this

        activity?.let {
            if (mediaBrowserCompat.isConnected.not()) {
                mediaBrowserCompat.connect()
            }
        }

        disposables += livePublisher
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    liveProgram = it
                }, {})
    }

    override fun onDestroy() {
        activity?.let { act ->
            disposables.clear()
            MediaControllerCompat.getMediaController(act)?.unregisterCallback(mediaCallback)
            mediaBrowserCompat.disconnect()
        }
        super.onDestroy()
    }
}