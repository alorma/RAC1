package com.alorma.rac1.ui.main

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.usecase.GetSchedulePrograms
import com.alorma.rac1.service.*
import com.alorma.rac1.ui.common.BasePresenter
import com.alorma.rac1.ui.common.diffDSL
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainPresenter @Inject constructor(private val getSchedule: GetSchedulePrograms,
                                        private val states: MainState,
                                        private val routes: MainRoute,
                                        private val livePublisher: BehaviorSubject<ProgramItem>,
                                        private val playbackPublisher: PublishSubject<StreamPlayback>) :
        BasePresenter<MainAction, MainRoute, MainState>() {

    private var currentStatus: StreamPlayback = Live
    private lateinit var currentProgram: ProgramItem
    private var currentSession: SessionDto? = null

    private val items = mutableListOf<ProgramItem>()

    override fun reduce(a: MainAction) {
        when (a) {
            MainAction.Load -> load()
            MainAction.Play -> {
                playbackPublisher.onNext(currentSession?.let {
                    Podcast(currentProgram, it)
                } ?: Live)
            }
            MainAction.PlayLive -> playbackPublisher.onNext(Live)
            MainAction.Stop -> playbackPublisher.onNext(Stop)
            is MainAction.ProgramSelected -> navigate(routes.programDetail(a.program, currentStatus !== Stop))
        }
    }

    private fun load() {
        subscribeToChange()

        loadItems(getSchedule.execute())

        disposable += livePublisher.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    this.currentProgram = it
                    render(states.live(it))
                }, {

                })
    }

    private fun loadItems(single: Single<List<ProgramItem>>) {
        disposable += single.map {
            val diffDSL = items.diffDSL {
                newList = it
                comparable = compareBy { it.id }
            }
            it to diffDSL
        }.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(states.scheduledPrograms(it.first, it.second))
                }, {
                    it.printStackTrace()
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
                        is Play -> {
                            if (it is Podcast) {
                                this.currentProgram = it.program
                                this.currentSession = it.sessionDto
                            }
                            render(states.playing(it, currentProgram))
                        }
                        is Stop -> render(states.stop())
                    }
                }, {})
    }
}