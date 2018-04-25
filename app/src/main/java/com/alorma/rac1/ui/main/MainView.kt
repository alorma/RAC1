package com.alorma.rac1.ui.main

import android.support.v7.util.DiffUtil
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.ui.common.Action
import com.alorma.rac1.ui.common.Route
import com.alorma.rac1.ui.common.State
import javax.inject.Inject

open class MainAction @Inject constructor() : Action() {
    object Load : MainAction()
    data class ProgramSelected(val program: ProgramItem) : MainAction()

    fun load(): MainAction = Load
    fun onProgramSelected(program: ProgramItem): MainAction = ProgramSelected(program)
}

open class MainState @Inject constructor() : State() {
    data class SuccessSchedule(val items: List<ProgramItem>,
                               val diffResult: DiffUtil.DiffResult) : MainState()

    fun applyDiff(items: List<ProgramItem>, diffResult: DiffUtil.DiffResult): MainState = SuccessSchedule(items, diffResult)
}

open class MainRoute @Inject constructor() : Route() {

    data class OpenProgramDetail(val program: ProgramItem) : MainRoute()

    fun programDetail(program: ProgramItem): MainRoute = OpenProgramDetail(program)
}