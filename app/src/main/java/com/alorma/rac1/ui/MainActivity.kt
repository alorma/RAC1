package com.alorma.rac1.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.program.LiveProgramFragment
import com.alorma.rac1.ui.program.ProgramActivity
import com.alorma.rac1.ui.programs.ProgramsFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.live_program_fragment.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ProgramsFragment.ListCallback,
        PlayConnectionFragment.PlayerCallback {

    private lateinit var programsFragment: ProgramsFragment
    private lateinit var liveFragment: LiveProgramFragment
    private lateinit var playConnectionFragment: PlayConnectionFragment
    private val disposable = CompositeDisposable()

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component inject this

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
        subscribeToLive()
    }

    private fun openSchedule() {
        supportFragmentManager.beginTransaction().replace(R.id.container, programsFragment).commit()
    }

    private fun openLive() {
        supportFragmentManager.beginTransaction().replace(R.id.currentProgram, liveFragment).commit()
    }

    override fun onProgramSelected(programItem: ProgramItem) {
        startActivity(ProgramActivity.getIntent(this, programItem.id, playConnectionFragment.isPlaying))
    }

    override fun onPlayPlayback() {
        playerControlsLy.visibility = View.VISIBLE
    }

    override fun onStopPlayback() {
        playerControlsLy.visibility = View.GONE
    }

    private fun subscribeToLive() {
        disposable += livePublisher.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    programName.text = it.title
                }, {

                })
    }
}
