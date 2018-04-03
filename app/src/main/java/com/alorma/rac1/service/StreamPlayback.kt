package com.alorma.rac1.service

import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem

sealed class StreamPlayback
object Stop : StreamPlayback()

sealed class Play(val programItem: ProgramItem) : StreamPlayback()
class Live(program: ProgramItem) : Play(program)
data class Podcast(val program: ProgramItem, val sessionDto: SessionDto) : Play(program)