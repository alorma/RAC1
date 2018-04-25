package com.alorma.rac1.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.service.*
import com.alorma.rac1.ui.common.BaseView
import com.alorma.rac1.ui.common.ProgramsAdapter
import com.alorma.rac1.ui.main.MainAction
import com.alorma.rac1.ui.main.MainPresenter
import com.alorma.rac1.ui.main.MainRoute
import com.alorma.rac1.ui.main.MainState
import com.alorma.rac1.ui.program.LiveProgramFragment
import com.alorma.rac1.ui.program.ProgramActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.player_control_fragment.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), PlayConnectionFragment.PlayerCallback, BaseView<MainRoute, MainState> {

    @Inject
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var actions: MainAction

    private lateinit var liveFragment: LiveProgramFragment
    private lateinit var playConnectionFragment: PlayConnectionFragment
    private val disposable = CompositeDisposable()

    private var currentStatus: StreamPlayback = Live
    private var currentLiveProgram: ProgramItem? = null

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    private val adapter: ProgramsAdapter by lazy {
        ProgramsAdapter {
            presenter reduce actions.onProgramSelected(it)
        }
    }
    lateinit var manager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component inject this

        liveFragment = LiveProgramFragment()

        playConnectionFragment = PlayConnectionFragment().apply {
            playerCallback = this@MainActivity
        }

        supportFragmentManager.beginTransaction().apply {
            add(playConnectionFragment, playConnectionFragment::class.java.simpleName)
        }.commitNow()

        initView()

        presenter init this

        presenter reduce actions.load()

        openLive()

        subscribeToLive()
        subscribeToChange()
    }

    private fun initView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return false
            }
        }.also { manager = it }
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.very_dark_grey)))
        })
    }

    private fun openLive() {
        supportFragmentManager.beginTransaction().replace(R.id.currentProgram, liveFragment).commit()
    }

    fun onProgramSelected(programItem: ProgramItem) {
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
                    this.currentLiveProgram = it
                    if (currentStatus === Live) {
                        programName.text = it.title
                    }
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
            programTime.max = it.sessionDto.durationSeconds.toInt()
            programTime.progress = programTime.max / 2
            programTime.visibility = View.VISIBLE
        } else if (currentLiveProgram != null) {
            programTime.visibility = View.GONE
            currentLiveProgram?.let {
                programName.text = it.title
            }
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

    override fun render(s: MainState) {
        when (s) {
            is MainState.SuccessSchedule -> {
                recyclerView?.let {
                    val first = manager.findFirstVisibleItemPosition()
                    recyclerView.recycledViewPool.clear()
                    adapter.setItems(s.items)
                    s.diffResult.dispatchUpdatesTo(adapter)
                    recyclerView.scrollToPosition(first)
                }
            }
        }
    }

    override fun navigate(r: MainRoute) {
        when (r) {
            is MainRoute.OpenProgramDetail -> {
                startActivity(ProgramActivity.getIntent(this, r.program.id,
                        playConnectionFragment.isPlaying))
            }
        }
    }
}
