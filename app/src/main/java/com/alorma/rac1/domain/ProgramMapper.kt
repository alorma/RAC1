package com.alorma.rac1.domain

import com.alorma.rac1.data.net.ProgramDto
import com.alorma.rac1.data.net.WrappedProgram
import com.alorma.rac1.data.schedule.Schedule
import com.alorma.rac1.data.schedule.ScheduleParser
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

    fun map(dto: WrappedProgram) = ProgramItem(
            dto.program.id,
            dto.program.title,
            dto.program.subtitle,
            dto.program.description,
            map(dto.program.schedule),
            dto.program.socialNetworks,
            dto.program.images,
            dto.program.url,
            dateTimes(dto)
    )

    private fun dateTimes(dto: WrappedProgram): Times {
        return Times(dateFormatter.mapDate(dto.start), dateFormatter.mapDate(dto.end), dateFormatter.mapDuration(dto.duration))

    }

    private fun map(schedule: String?): Schedule? {
        return if (schedule != null && schedule.isNotBlank()) {
            scheduleParser.parse(schedule)
        } else {
            null
        }
    }
}