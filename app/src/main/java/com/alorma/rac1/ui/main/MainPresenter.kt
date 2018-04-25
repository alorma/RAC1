package com.alorma.rac1.ui.main

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.usecase.GetNow
import com.alorma.rac1.domain.usecase.GetSchedulePrograms
import com.alorma.rac1.service.Live
import com.alorma.rac1.service.Play
import com.alorma.rac1.service.Stop
import com.alorma.rac1.service.StreamPlayback
import com.alorma.rac1.ui.common.BasePresenter
import com.alorma.rac1.ui.common.diffDSL
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainPresenter @Inject constructor(private val getNow: GetNow,
                                        private val getSchedule: GetSchedulePrograms,
                                        private val states: MainState,
                                        private val routes: MainRoute,
                                        private val playbackPublisher: PublishSubject<StreamPlayback>) :
        BasePresenter<MainAction, MainRoute, MainState>() {

    private var currentStatus: StreamPlayback = Live
    private lateinit var currentLiveProgram: ProgramItem
    private val items = mutableListOf<ProgramItem>()

    override fun reduce(a: MainAction) {
        when (a) {
            MainAction.Load -> load()
            MainAction.PlayLive -> playbackPublisher.onNext(Live)
            is MainAction.ProgramSelected -> navigate(routes.programDetail(a.program))
        }
    }


    private fun load() {
        subscribeToChange()

        loadItems(getSchedule.execute())

        disposable += getNow.execute()
                .observeOnUI()
                .subscribe({
                    this.currentLiveProgram = it
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
                        is Play -> render(states.playing(it, currentLiveProgram))
                        //Stop -> showNoPlaying()
                    }
                }, {})
    }
}