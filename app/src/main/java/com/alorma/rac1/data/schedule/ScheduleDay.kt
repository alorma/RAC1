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

class Monday : ScheduleDay("dilluns")
class Tuesday : ScheduleDay("dimarts")
class Wednesday : ScheduleDay("dimecres")
class Thursday : ScheduleDay("dijous")
class Friday : ScheduleDay("divendres")
class Saturday : ScheduleDay("dissabte", "dissabtes")
class Sunday : ScheduleDay("diumenge", "diumenges")