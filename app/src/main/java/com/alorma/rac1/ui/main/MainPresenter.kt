package com.alorma.rac1.ui.main

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.usecase.GetNow
import com.alorma.rac1.domain.usecase.GetSchedulePrograms
import com.alorma.rac1.ui.common.BasePresenter
import com.alorma.rac1.ui.common.diffDSL
import io.reactivex.Single
import javax.inject.Inject

class MainPresenter @Inject constructor(private val getNow: GetNow,
                                        private val getSchedule: GetSchedulePrograms,
                                        private val states: MainState,
                                        private val routes: MainRoute) :
        BasePresenter<MainAction, MainRoute, MainState>() {

    private val items = mutableListOf<ProgramItem>()

    override fun reduce(a: MainAction) {
        when (a) {
            MainAction.Load -> load()
            is MainAction.ProgramSelected -> navigate(routes.programDetail(a.program))
        }
    }

    private fun load() {
        loadItems(getSchedule.execute())
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
                    render(states.applyDiff(it.first, it.second))
                }, {
                    it.printStackTrace()
                })
    }
}