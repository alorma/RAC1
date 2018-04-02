package com.alorma.rac1.domain

import com.alorma.rac1.data.net.ImagesDto
import com.alorma.rac1.data.net.SocialNetworksDto
import com.alorma.rac1.data.schedule.Schedule
import org.threeten.bp.LocalDateTime

data class ProgramItem(
        val id: String,
        val title: String,
        val subtitle: String,
        val description: String?,
        val schedule: Schedule?,
        val socialNetworks: SocialNetworksDto,
        val images: ImagesDto,
        val url: String?,
        val times: Times?
)

data class Times(val start: LocalDateTime, val end: LocalDateTime, val duration: Long)