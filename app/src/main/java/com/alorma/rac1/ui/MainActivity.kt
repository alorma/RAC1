package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alorma.rac1.R
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ComponentName
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import com.alorma.rac1.service.LiveRadioService


class MainActivity : AppCompatActivity() {

    private val mediaBrowserCompat: MediaBrowserCompat by lazy {
        MediaBrowserCompat(this, ComponentName(this, LiveRadioService::class.java), mediaBrowserCompatConnectionCallback, null)
    }
    private var isPlaying: Boolean = false

    private val mediaCallback: MediaControllerCompat.Callback by lazy {
        object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                super.onPlaybackStateChanged(state)

                when (state.state) {
                    PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> {

                        setPlayIcon()
                        nowPlaying.visibility = View.GONE
                    }
                    PlaybackStateCompat.STATE_ERROR -> {
                    }
                    else -> {
                        setPauseIcon()
                        nowPlaying.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private val mediaBrowserCompatConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            try {
                // Create a MediaControllerCompat
                val mediaController = MediaControllerCompat(this@MainActivity, mediaBrowserCompat.sessionToken)

                // Save the controller
                MediaControllerCompat.setMediaController(this@MainActivity, mediaController)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(bottomBar) {
            configScheduleButton()
            configCenterButton()
            configProgramsButton()

            setSpaceOnClickListener(object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                    if (isPlaying) {
                        MediaControllerCompat.getMediaController(this@MainActivity)?.transportControls?.stop()
                    } else {
                        MediaControllerCompat.getMediaController(this@MainActivity)?.transportControls?.play()
                    }
                    isPlaying = isPlaying.not()
                }

                override fun onItemReselected(itemIndex: Int, itemName: String?) {

                }

                override fun onItemClick(itemIndex: Int, itemName: String?) {
                    when (itemIndex) {
                        0 -> openSchedule()
                        1 -> openPrograms()
                    }
                }
            })
            openSchedule()
        }

        MediaControllerCompat.getMediaController(this)?.registerCallback(mediaCallback)
    }

    private fun openSchedule() {

    }

    private fun openPrograms() {

    }

    private fun setPauseIcon() {
        with(bottomBar) {
            changeCenterButtonIcon(R.drawable.ic_stop)
            (ContextCompat.getColor(this@MainActivity, R.color.white))
        }
    }

    private fun setPlayIcon() {
        with(bottomBar) {
            changeCenterButtonIcon(R.drawable.ic_play)
            (ContextCompat.getColor(this@MainActivity, R.color.white))
        }
    }

    private fun SpaceNavigationView.configScheduleButton() {
        val space = getSpace(R.string.navigation_schedule, R.drawable.ic_schedule)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configProgramsButton() {
        val space = getSpace(R.string.navigation_programs, R.drawable.ic_list)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configCenterButton() {
        setCentreButtonIcon(R.drawable.ic_play)
    }

    private fun getSpace(title: Int, icon: Int): SpaceItem = SpaceItem(resources.getString(title), icon)

    override fun onSaveInstanceState(outState: Bundle?) {
        bottomBar.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mediaBrowserCompat.connect()
    }

    override fun onStop() {
        MediaControllerCompat.getMediaController(this)
                ?.unregisterCallback(mediaCallback)
        mediaBrowserCompat.disconnect()
        super.onStop()
    }
}
