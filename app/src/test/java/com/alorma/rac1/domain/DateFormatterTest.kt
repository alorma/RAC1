package com.alorma.rac1.domain

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
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
}