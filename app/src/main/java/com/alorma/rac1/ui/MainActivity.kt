package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alorma.rac1.R
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.program.ProgramDetailFragment
import com.alorma.rac1.ui.programs.ProgramsFragment
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ProgramsFragment.ListCallback, ProgramDetailFragment.DetailCallback, LivePlayerFragment.PlayerCallback {

    private lateinit var fragment: ProgramsFragment
    private lateinit var livePlayerFragment: LivePlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = ProgramsFragment().apply {
            listCallback = this@MainActivity
        }

        livePlayerFragment = LivePlayerFragment().apply {
            playerCallback = this@MainActivity
        }

        with(supportFragmentManager.beginTransaction()) {
            add(livePlayerFragment, LivePlayerFragment::class.java.simpleName)
            replace(R.id.container, fragment).commitNow()
        }

        with(bottomBar) {
            configScheduleButton()
            configCenterButton()
            configProgramsButton()

            setSpaceOnClickListener(object : SpaceOnClickListener {
                override fun onCentreButtonClick() {
                    livePlayerFragment.togglePlay()
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
        }

        openSchedule()
    }

    private fun openSchedule() {
        fragment.loadSchedule()
    }

    private fun openPrograms() {
        fragment.loadPrograms()
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

    override fun onSessionCallback(programItem: ProgramItem, session: SessionDto) {
        Toast.makeText(this, "Play: ${programItem.title} - ${session.title}", Toast.LENGTH_SHORT).show()
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
