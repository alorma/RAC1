package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alorma.rac1.R
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.now.NowPlayingFragment
import com.alorma.rac1.ui.program.ProgramDetailFragment
import com.alorma.rac1.ui.programs.ProgramsFragment
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ProgramsFragment.ListCallback,
        ProgramDetailFragment.DetailCallback, PlayConnectionFragment.PlayerCallback {

    private lateinit var programsFragment: ProgramsFragment
    private lateinit var PlayConnectionFragment: PlayConnectionFragment
    private lateinit var nowPlayingFragment: NowPlayingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        programsFragment = ProgramsFragment().apply {
            listCallback = this@MainActivity
        }

        PlayConnectionFragment = PlayConnectionFragment().apply {
            playerCallback = this@MainActivity
        }

        nowPlayingFragment = NowPlayingFragment()

        supportFragmentManager.beginTransaction().apply {
            add(PlayConnectionFragment, PlayConnectionFragment::class.java.simpleName)
            replace(R.id.nowPlaying, nowPlayingFragment)
        }.commitNow()

        with(bottomBar) {
            configScheduleButton()
            configInfiniteButton()
            configCenterButton()
            configPlayListButton()
            configFavListButton()

            showIconOnly()

            setSpaceOnClickListener(object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                    PlayConnectionFragment.togglePlay()
                }

                override fun onItemReselected(itemIndex: Int, itemName: String?) {

                }

                override fun onItemClick(itemIndex: Int, itemName: String?) {
                    when (itemIndex) {
                        0 -> openSchedule()
                        else -> openOther()
                    }
                }
            })
        }

        openSchedule()
    }

    private fun openSchedule() {
        supportFragmentManager.beginTransaction().replace(R.id.container, programsFragment).commit()
    }

    private fun openOther() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, Fragment())
        }.commitNow()
    }

    private fun setStopIcon() {
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

    private fun SpaceNavigationView.configInfiniteButton() {
        val space = getSpace(R.string.navigation_actual, R.drawable.ic_infinite)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configPlayListButton() {
        val space = getSpace(R.string.navigation_playlist, R.drawable.ic_playlist)
        addSpaceItem(space)
    }

    private fun SpaceNavigationView.configFavListButton() {
        val space = getSpace(R.string.navigation_favorites, R.drawable.ic_star_empty)
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
        val fragment = ProgramDetailFragment().apply {
            program = programItem
            detailCallback = this@MainActivity
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onProgramDetailBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onProgramDetailError(title: String) {
        val error = resources.getString(R.string.error_laoding_program_detail, title)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    override fun onPlayPlayback() {
        setStopIcon()
        nowPlaying.visibility = View.VISIBLE
    }

    override fun onStopPlayback() {
        setPlayIcon()
        nowPlaying.visibility = View.GONE
    }
}
