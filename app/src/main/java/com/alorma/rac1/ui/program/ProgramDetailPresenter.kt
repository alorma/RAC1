package com.alorma.rac1.ui.program

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramSection
import com.alorma.rac1.domain.SessionsRepository
import com.alorma.rac1.ui.common.BasePresenter
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class ProgramDetailPresenter @Inject constructor(
        private val sessionsRepository: SessionsRepository)
    : BasePresenter<ProgramDetailAction, ProgramDetailRoute, ProgramDetailState>() {

    override fun reduce(a: ProgramDetailAction) {
        when (a) {
            is ProgramDetailAction.Load -> onLoad(a)
        }
    }


    private fun onLoad(load: ProgramDetailAction.Load) {
        disposable += Flowable.fromIterable(load.program.sections)
                .map { it to listOf<SessionDto>() }
                .toList().map { it.toMap() }.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(ProgramDetailState.Success(it))
                    loadSections(load.program.id, it.keys)
                }, {

                })
    }

    private fun loadSections(program: String, set: Set<ProgramSection>) {
        disposable += Flowable.fromIterable(set)
                .flatMapSingle { section ->
                    sessionsRepository.getSessions(program, section.id)
                            .map { section to it.result }
                }
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(ProgramDetailState.SessionsLoaded(it.first, it.second))
                }, {

                })
    }
}