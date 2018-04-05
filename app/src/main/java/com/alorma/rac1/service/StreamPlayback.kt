package com.alorma.rac1.service

import com.alorma.rac1.data.net.SessionDto
import com.alorma.rac1.domain.ProgramItem

sealed class StreamPlayback
object Stop : StreamPlayback()

sealed class Play: StreamPlayback()
object Live : Play()
data class Podcast(val program: ProgramItem, val sessionDto: SessionDto) : Play()