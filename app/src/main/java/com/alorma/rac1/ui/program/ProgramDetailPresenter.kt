package com.alorma.rac1.ui.program

import com.alorma.rac1.commons.observeOnUI
import com.alorma.rac1.commons.plusAssign
import com.alorma.rac1.commons.subscribeOnIO
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.SessionsRepository
import com.alorma.rac1.ui.common.BasePresenter
import io.reactivex.Flowable
import javax.inject.Inject

class ProgramDetailPresenter @Inject constructor(
        private val sessionsRepository: SessionsRepository)
    : BasePresenter<ProgramDetailAction, ProgramDetailRoute, ProgramDetailState>() {

    private lateinit var program: ProgramItem

    override fun reduce(a: ProgramDetailAction) {
        when (a) {
            is ProgramDetailAction.Load -> onLoad(a)
            is ProgramDetailAction.LoadSection -> loadSection(program.id, a.section.id)
        }
    }

    private fun onLoad(load: ProgramDetailAction.Load) {
        this.program = load.program
        disposable += Flowable.fromIterable(load.program.sections)
                .map { it to listOf<SessionDto>() }
                .toList().map { it.toMap() }.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(ProgramDetailState.Success(it))
                }, {

                })
    }

    private fun loadSection(program: String, section: String) {
        disposable += sessionsRepository.getSessions(program, section)
                .map { section to it.result }
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(ProgramDetailState.SessionsLoaded(it.first, it.second))
                }, {

                })
    }
}