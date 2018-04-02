package com.alorma.rac1.data.schedule

sealed class ScheduleDay(
        val name: String,
        private vararg val others: String) {

    fun isOnText(text: String): Boolean {
        val lowerCase = text.toLowerCase()

        val baseName = lowerCase.contains(name.toLowerCase())

        val othersContains = others.firstOrNull {
            lowerCase.contains(it.toLowerCase())
        }

        return baseName || othersContains != null
    }
}

object Monday : ScheduleDay("dilluns")
object Tuesday : ScheduleDay("dimarts")
object Wednesday : ScheduleDay("dimecres")
object Thursday : ScheduleDay("dijous")
object Friday : ScheduleDay("divendres")
object Saturday : ScheduleDay("dissabte", "dissabtes")
object Sunday : ScheduleDay("diumenge", "diumenges")