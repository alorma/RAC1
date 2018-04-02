package com.alorma.rac1.domain

import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Duration
import org.threeten.bp.Month

class DateFormatterTest {

    private lateinit var formatter: DateFormatter

    @Before
    fun setup() {
        formatter = DateFormatter()
    }

    // 2018-04-02T22:30:00+02:00

    @Test
    fun givenDate_2018_04_02_22_30_returnValidDate() {
        val date = formatter.mapDate("2018-04-02T22:30:00+02:00")

        assertEquals(2018, date.year)
        assertEquals(Month.APRIL, date.month)
        assertEquals(2, date.dayOfMonth)

        assertEquals(22, date.hour)
        assertEquals(30, date.minute)
    }

    // PT2H30M

    @Test
    fun givenDuration_2h_30m_return2Hours30Min() {
        val duration = formatter.mapDuration("PT2H30M")

        assertEquals(2, duration.toHours())
        assertEquals(30, duration.asMinutes())
    }
    // PT0S

    @Test
    fun givenDuration_0s_return0seconds() {
        val duration = formatter.mapDuration("PT0S")

        assertEquals(0, duration.toHours())
        assertEquals(0, duration.toMinutes())
    }
}