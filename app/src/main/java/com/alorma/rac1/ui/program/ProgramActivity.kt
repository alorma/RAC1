package com.alorma.rac1.ui.program

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.service.*
import com.alorma.rac1.ui.common.dsl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.player_control_fragment.*
import kotlinx.android.synthetic.main.program_toolbar.*
import javax.inject.Inject

class ProgramActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_PLAYING = "extra_playing"

        fun getIntent(context: Context, programId: String, playing: Boolean): Intent =
                Intent(context, ProgramActivity::class.java).apply {
                    putExtra(EXTRA_ID, programId)
                    putExtra(EXTRA_PLAYING, playing)
                }
    }

    @Inject
    lateinit var programsRepository: ProgramsRepository

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    private var currentStatus: StreamPlayback = Live

    private lateinit var infoFragment: ProgramInfoFragment
    private lateinit var podcastFragment: ProgramPodcastFragment

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_activity)

        component inject this

        intent?.getStringExtra(EXTRA_ID)?.let { programId ->
            infoFragment = ProgramInfoFragment()
            podcastFragment = ProgramPodcastFragment()

            toolbar.dsl {
                back { action = { finish() } }
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> selectInfoTab()
                        1 -> selectPodcastTab()
                    }
                }
            })
            loadProgram(programId)

            if (intent?.getBooleanExtra(EXTRA_PLAYING, false) == true) {
                showPlaying(Stop)
            } else {
                showNoPlaying()
            }

            subscribeToLive()
            subscribeToChange()

        } ?: finish()

    }

    private fun subscribeToLive() {
        disposable += livePublisher.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    programName.text = it.title
                }, {

                })
    }

    private fun subscribeToChange() {
        disposable += playbackPublisher.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    if (it !== Stop) {
                        this.currentStatus = it
                    }
                    when (it) {
                        is Play -> showPlaying(it)
                        Stop -> showNoPlaying()
                    }
                }, {})

    }

    private fun showPlaying(it: StreamPlayback) {
        if (it is Podcast) {
            programName.text = it.sessionDto.title
        }
        playIcon.setImageResource(R.drawable.ic_stop)
        playIcon.setOnClickListener {
            playbackPublisher.onNext(Stop)
        }
    }

    private fun showNoPlaying() {
        playIcon.setImageResource(R.drawable.ic_play)
        playIcon.setOnClickListener {
            if (currentStatus !== Stop) {
                playbackPublisher.onNext(currentStatus)
            }
        }
    }

    private fun loadProgram(programId: String) {
        disposable += programsRepository.getProgram(programId)
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    onProgramSet(it)
                }, {})
    }

    private fun onProgramSet(it: ProgramItem) {
        infoFragment.updateProgram(it)
        podcastFragment.updateProgram(it)
        selectInfoTab()
    }

    private fun selectInfoTab() {
        supportFragmentManager.beginTransaction().replace(R.id.content, infoFragment).commit()
    }

    private fun selectPodcastTab() {
        supportFragmentManager.beginTransaction().replace(R.id.content, podcastFragment).commit()
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

}