package com.alorma.rac1.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alorma.rac1.R
import com.alorma.rac1.service.LiveRadioService
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var isPlaying: Boolean = false

    var liveRadioService: LiveRadioService? = null

    private val liveConnection: ServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {
                liveRadioService = null
            }

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                val binder = service as LiveRadioService.LiveBinder
                liveRadioService = binder.getService()
            }
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
                        pausePlayback()
                    } else {
                        playPlayback()
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

        val intent = Intent(this, LiveRadioService::class.java)
        bindService(intent, liveConnection, Context.BIND_AUTO_CREATE)
    }

    private fun playPlayback() {
        liveRadioService?.play()
        setPauseIcon()
        nowPlaying.visibility = View.VISIBLE
    }

    private fun pausePlayback() {
        liveRadioService?.pause()
        setPlayIcon()
        nowPlaying.visibility = View.GONE
    }

    private fun openSchedule() {

    }

    private fun openPrograms() {

    }

    private fun setPauseIcon() {
        with(bottomBar) {
            changeCenterButtonIcon(R.drawable.ic_pause)
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

    override fun onStop() {
        unbindService(liveConnection)
        super.onStop()
    }
}
