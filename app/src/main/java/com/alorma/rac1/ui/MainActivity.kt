package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alorma.rac1.R
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.program.LiveProgramFragment
import com.alorma.rac1.ui.program.ProgramActivity
import com.alorma.rac1.ui.programs.ProgramsFragment
import com.luseen.spacenavigation.SpaceItem

class MainActivity : AppCompatActivity(), ProgramsFragment.ListCallback,
        PlayConnectionFragment.PlayerCallback {

    private lateinit var programsFragment: ProgramsFragment
    private lateinit var liveFragment: LiveProgramFragment
    private lateinit var playConnectionFragment: PlayConnectionFragment

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

        openLive()
        openSchedule()
    }

    private fun openSchedule() {
        supportFragmentManager.beginTransaction().replace(R.id.container, programsFragment).commit()
    }

    private fun openLive() {
        supportFragmentManager.beginTransaction().replace(R.id.currentProgram, liveFragment).commit()
    }

    private fun getSpace(title: Int, icon: Int): SpaceItem = SpaceItem(resources.getString(title), icon)

    override fun onProgramSelected(programItem: ProgramItem) {
        startActivity(ProgramActivity.getIntent(this, programItem.id, playConnectionFragment.isPlaying))
    }

    override fun onPlayPlayback() {
        //bottomBar.changeCenterButtonIcon(R.drawable.ic_stop)
    }

    override fun onStopPlayback() {
        //bottomBar.changeCenterButtonIcon(R.drawable.ic_play)
    }
}
