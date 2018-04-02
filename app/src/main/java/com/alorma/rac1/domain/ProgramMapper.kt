package com.alorma.rac1.domain

import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.data.schedule.Schedule
import com.alorma.rac1.data.schedule.ScheduleParser
import java.util.*
import javax.inject.Inject

class ProgramMapper @Inject constructor(
        private val scheduleParser: ScheduleParser,
        private val dateFormatter: DateFormatter
) {

    fun map(dto: ProgramDto) = ProgramItem(
            dto.id,
            dto.title,
            dto.subtitle,
            dto.description,
            map(dto.schedule),
            dto.socialNetworks,
            dto.images,
            dto.url,
            null
    )

    fun map(dto: ProgramDto, start: String, end: String, duration: String) = ProgramItem(
            dto.id,
            dto.title,
            dto.subtitle,
            dto.description,
            map(dto.schedule),
            dto.socialNetworks,
            dto.images,
            dto.url,
            dateTimes(start, end, duration)
    )

    private fun dateTimes(start: String, end: String, duration: String): Times? {
        return Times(dateFormatter.mapDate(start), dateFormatter.mapDate(end), 0)
    }

    private fun map(schedule: String?): Schedule? {
        return if (schedule != null && schedule.isNotBlank()) {
            scheduleParser.parse(schedule)
        } else {
            null
        }
    }
}