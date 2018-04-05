package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.alorma.rac1.R
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.program.LiveProgramFragment
import com.alorma.rac1.ui.program.ProgramActivity
import com.alorma.rac1.ui.programs.ProgramsFragment
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProgramsFragment.ListCallback,
        PlayConnectionFragment.PlayerCallback {

    private lateinit var programsFragment: ProgramsFragment
    private lateinit var liveFragment: LiveProgramFragment
    private lateinit var playConnectionFragment: PlayConnectionFragment

    private var lastFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        programsFragment = ProgramsFragment().apply {
            listCallback = this@MainActivity
        }

        liveFragment = LiveProgramFragment()

        playConnectionFragment = PlayConnectionFragment().apply {
            playerCallback = this@MainActivity
        }

        supportFragmentManager.beginTransaction().apply {
            add(playConnectionFragment, playConnectionFragment::class.java.simpleName)
        }.commitNow()

        with(bottomBar) {
            initWithSaveInstanceState(savedInstanceState)
            configScheduleButton()
            configLiveButton()
            configCenterButton()

            changeCurrentItem(1)
            showIconOnly()

            setSpaceOnClickListener(object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                    playConnectionFragment.togglePlay()
                }

                override fun onItemReselected(itemIndex: Int, itemName: String?) {

                }

                override fun onItemClick(itemIndex: Int, itemName: String?) {
                    when (itemIndex) {
                        0 -> openSchedule()
                        1 -> openLive()
                    }
                }
            })
        }

        openLive()
    }

    private fun openSchedule() {
        addFragment(programsFragment)
    }

    private fun openLive() {
        addFragment(liveFragment)
    }

    private fun addFragment(fragment: Fragment, backStack: Boolean = false) {
        this.lastFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            if (backStack) {
                addToBackStack(null)
            }
        }.commit()
    }

    private fun SpaceNavigationView.configScheduleButton() {
        val space = getSpace(R.string.navigation_schedule, R.drawable.ic_schedule)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configLiveButton() {
        val space = getSpace(R.string.navigation_actual, R.drawable.ic_radio)
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

    override fun onProgramSelected(programItem: ProgramItem) {
        startActivity(ProgramActivity.getIntent(this, programItem.id, playConnectionFragment.isPlaying))
    }

    override fun onPlayPlayback() {
        bottomBar.changeCenterButtonIcon(R.drawable.ic_stop)
    }

    override fun onStopPlayback() {
        bottomBar.changeCenterButtonIcon(R.drawable.ic_play)
    }
}
