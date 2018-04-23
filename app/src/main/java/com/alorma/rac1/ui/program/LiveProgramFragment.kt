package com.alorma.rac1.ui.program

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.rac1.R
import com.alorma.rac1.Rac1Application.Companion.component
import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.service.Live
import com.alorma.rac1.service.Play
import com.alorma.rac1.service.Stop
import com.alorma.rac1.service.StreamPlayback
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.live_program_fragment.*
import javax.inject.Inject

class LiveProgramFragment : Fragment() {

    private lateinit var infoFragment: ProgramInfoFragment
    private lateinit var podcastFragment: ProgramPodcastFragment

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var livePublisher: BehaviorSubject<ProgramItem>

    @Inject
    lateinit var playbackPublisher: PublishSubject<StreamPlayback>

    @Inject
    lateinit var programsRepository: ProgramsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component inject this

        infoFragment = ProgramInfoFragment()
        podcastFragment = ProgramPodcastFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.live_program_fragment, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playButton.setOnClickListener {
            playbackPublisher.onNext(Live)
        }
    }

    override fun onStart() {
        super.onStart()
        connectToLiveUpdate()
        subscribeToPlaybackStream()
    }

    private fun connectToLiveUpdate() {
        disposable += livePublisher
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    setTitle(it)
                    infoFragment.updateProgram(it)
                    podcastFragment.updateProgram(it)
                }, {}, {})
    }

    private fun subscribeToPlaybackStream() {
        disposable += playbackPublisher.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    if (it === Stop) {
                        playButton.isEnabled = true
                        playButton.visibility = View.VISIBLE
                    } else {
                        playButton.isEnabled = false
                        playButton.visibility = View.GONE
                    }
                }, {})
    }

    private fun setTitle(it: ProgramItem) {
        programName.text = it.title

        Glide.with(programImage.context)
                .load(it.images.person)
                .into(programImage)

        programSchedule.text = it.scheduleText
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}