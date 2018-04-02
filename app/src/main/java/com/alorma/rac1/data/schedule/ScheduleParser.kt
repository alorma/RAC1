package com.alorma.rac1.data.schedule

import javax.inject.Inject

class ScheduleParser @Inject constructor() {
    private val days = setOf(
            Monday,
            Tuesday,
            Wednesday,
            Thursday,
            Friday,
            Saturday,
            Sunday
    )

    fun parse(text: String): Schedule {
        val daysList = days.filter {
            it.isOnText(text)
        }

        return if (daysList.size > 1) {
            var accept = false
            val daysSet = mutableSetOf<ScheduleDay>()

            days.forEach {
                if (accept && daysList.contains(it)) {
                    daysSet.add(it)
                    accept = false
                } else if (daysList.contains(it)) {
                    accept = true
                }

                if (accept) {
                    daysSet.add(it)
                }
            }

            Schedule(daysSet.toList())
        } else {
            Schedule(daysList)
        }
    }
}
