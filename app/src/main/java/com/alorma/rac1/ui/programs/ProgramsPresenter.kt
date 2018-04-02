package com.alorma.rac1.ui.programs

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramsRepository
import com.alorma.rac1.ui.common.BasePresenter
import com.alorma.rac1.ui.common.diffDSL
import io.reactivex.Single
import javax.inject.Inject

class ProgramsPresenter @Inject constructor(
        private val programsRepository: ProgramsRepository)
    : BasePresenter<ProgramsAction, ProgramsRoute, ProgramsState>() {

    private val items = mutableListOf<ProgramItem>()

    override infix fun reduce(a: ProgramsAction) {
        when (a) {
            is ProgramsAction.LoadSchedule -> loadSchedules()
            is ProgramsAction.LoadPrograms -> loadPrograms()
            is ProgramsAction.ProgramSelected -> navigate(ProgramsRoute.OpenProgramDetail(a.it))
        }
    }

    private fun loadSchedules() {
        loadItems(programsRepository.getSchedule())
    }

    private fun loadPrograms() {
        loadItems(programsRepository.getPrograms())
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
                    render(ProgramsState.ApplyDiff(it.first, it.second))
                }, {
                    it.printStackTrace()
                })
    }
}