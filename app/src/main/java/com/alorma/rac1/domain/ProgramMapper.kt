package com.alorma.rac1.domain

import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.data.schedule.Schedule
import com.alorma.rac1.data.schedule.ScheduleParser
import javax.inject.Inject

class ProgramMapper @Inject constructor(
        private val scheduleParser: ScheduleParser) {

    fun map(dto: ProgramDto) = ProgramItem(
            dto.id,
            dto.title,
            dto.subtitle,
            dto.description,
            map(dto.schedule),
            dto.socialNetworks,
            dto.images,
            dto.url
    )

    private fun map(schedule: String?): Schedule? {
        return if (schedule != null && schedule.isNotBlank()) {
            scheduleParser.parse(schedule)
        } else {
            null
        }
    }
}