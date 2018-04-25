package com.alorma.rac1.ui.main

import android.support.v7.util.DiffUtil
import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.service.Live
import com.alorma.rac1.service.Play
import com.alorma.rac1.service.Podcast
import com.alorma.rac1.ui.common.Action
import com.alorma.rac1.ui.common.Route
import com.alorma.rac1.ui.common.State
import javax.inject.Inject

open class MainAction @Inject constructor() : Action() {
    object Load : MainAction()
    object PlayLive : MainAction()
    object Play : MainAction()
    object Stop : MainAction()
    data class ProgramSelected(val program: ProgramItem) : MainAction()

    fun load(): MainAction = Load
    fun onProgramSelected(program: ProgramItem): MainAction = ProgramSelected(program)
    fun play(): MainAction = Play
    fun playLive(): MainAction = PlayLive
    fun stop(): MainAction = Stop
}

open class MainState @Inject constructor() : State() {
    data class SuccessSchedule(val items: List<ProgramItem>,
                               val diffResult: DiffUtil.DiffResult) : MainState()

    data class SuccessLive(val program: ProgramItem) : MainState()

    sealed class PlayingStatus(open val program: ProgramItem) : MainState() {
        data class PlayingLive(override val program: ProgramItem) : PlayingStatus(program)
        data class PlayingPodcast(override val program: ProgramItem, val sessionDto: SessionDto) : PlayingStatus(program)
    }
    object Stop: MainState()

    fun scheduledPrograms(items: List<ProgramItem>, diffResult: DiffUtil.DiffResult): MainState = SuccessSchedule(items, diffResult)

    fun live(it: ProgramItem): MainState = SuccessLive(it)

    fun playing(it: Play, currentLiveProgram: ProgramItem): MainState = when (it) {
        Live -> PlayingStatus.PlayingLive(currentLiveProgram)
        is Podcast -> PlayingStatus.PlayingPodcast(it.program, it.sessionDto)
    }

    fun stop(): MainState = Stop
}

open class MainRoute @Inject constructor() : Route() {

    data class OpenProgramDetail(val program: ProgramItem, val isPlaying: Boolean) : MainRoute()

    fun programDetail(program: ProgramItem, isPlaying: Boolean): MainRoute
            = OpenProgramDetail(program, isPlaying)
}