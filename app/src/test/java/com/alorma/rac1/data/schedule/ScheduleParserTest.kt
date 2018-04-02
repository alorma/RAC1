package com.alorma.rac1.data.schedule

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ScheduleParserTest {

    private lateinit var parser: ScheduleParser

    @Before
    fun setup() {
        parser = ScheduleParser()
    }

    @Test
    fun givenDeDillunsADivenders_return5Days() {
        val text = "De dilluns a divendres, de 6 a 12 h"

        val result = parser.parse(text)

        assertEquals(5, result.days.size)
    }

    @Test
    fun givenDissabtesiDiumenges_return2Days() {
        val text = "Dissabtes i diumenges, de 7 a 14 h"

        val result = parser.parse(text)

        assertEquals(2, result.days.size)
    }

    @Test
    fun givenAllDays_return7Days() {
        val text = "de dilluns a divendres de 22.30 a 1 h, dissabtes i diumenges de 23 a 1h"

        val result = parser.parse(text)

        assertEquals(7, result.days.size)
    }
}