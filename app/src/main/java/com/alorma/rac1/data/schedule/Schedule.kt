package com.alorma.rac1.data.schedule

data class Schedule(val days: List<ScheduleDay>) {
    operator fun contains(day: ScheduleDay): Boolean = days.contains(day)
}