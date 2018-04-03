package com.alorma.rac1.ui.program

import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem
import com.alorma.rac1.domain.ProgramSection
import com.alorma.rac1.ui.common.Action
import com.alorma.rac1.ui.common.Route
import com.alorma.rac1.ui.common.State

sealed class ProgramDetailAction : Action() {
    data class Load(val program: ProgramItem) : ProgramDetailAction()
    data class LoadSection(val section: ProgramSection) : ProgramDetailAction()
}

sealed class ProgramDetailState : State() {
    data class Success(val sections: Map<ProgramSection, List<SessionDto>>) : ProgramDetailState()
    data class SessionsLoaded(val section: String, val sessions: List<SessionDto>, val openSections: MutableSet<String>) : ProgramDetailState()
}

sealed class ProgramDetailRoute : Route() {

}