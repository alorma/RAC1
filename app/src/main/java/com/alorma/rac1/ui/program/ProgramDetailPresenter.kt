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

    private val mapSection: MutableMap<String, List<SessionDto>> = mutableMapOf()
    private val openSections: MutableSet<String> = mutableSetOf()

    private var loading: Boolean = false

    override fun reduce(a: ProgramDetailAction) {
        when (a) {
            is ProgramDetailAction.Load -> onLoad(a)
            is ProgramDetailAction.LoadSection -> loadSection(program.id, a.section.id)
        }
    }

    private fun onLoad(load: ProgramDetailAction.Load) {
        this.program = load.program
        disposable += Flowable.fromIterable(load.program.sections)
                .filter { it.active && it.hidden.not() }
                .map { it to listOf<SessionDto>() }
                .toList().map { it.toMap() }.subscribeOnIO()
                .observeOnUI()
                .subscribe({
                    render(ProgramDetailState.Success(it))
                }, {

                })
    }

    private fun loadSection(program: String, section: String) {
        if (loading.not()) {
            loading = true
            if (openSections.contains(section)) {
                openSections.remove(section)
            } else {
                openSections.add(section)
            }

            if (mapSection.containsKey(section)) {
                mapSection[section]?.let { items ->
                    render(ProgramDetailState.SessionsLoaded(section, items, openSections))
                }
                loading = false
            } else {
                disposable += sessionsRepository.getSessions(program, section)
                        .map { section to it.result }
                        .subscribeOnIO()
                        .observeOnUI()
                        .doOnSuccess {
                            mapSection[it.first] = it.second
                        }
                        .subscribe({
                            render(ProgramDetailState.SessionsLoaded(it.first, it.second, openSections))
                            loading = false
                        }, {

                        })
            }
        }
    }
}