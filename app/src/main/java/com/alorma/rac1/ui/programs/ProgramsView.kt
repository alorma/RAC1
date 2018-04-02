package com.alorma.rac1.ui.programs

import android.support.v7.util.DiffUtil
import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.ui.common.Action
import com.alorma.rac1.ui.common.Route
import com.alorma.rac1.ui.common.State

sealed class ProgramsAction : Action() {
    class LoadSchedule : ProgramsAction()
    class LoadPrograms : ProgramsAction()
    data class ProgramSelected(val programDto: ProgramDto) : ProgramsAction()
}

sealed class ProgramsState : State() {
    data class ApplyDiff(val items: List<ProgramDto>, val diffResult: DiffUtil.DiffResult) : ProgramsState()
}

sealed class ProgramsRoute : Route() {
    data class OpenProgramDetail(val programDto: ProgramDto): ProgramsRoute()
}