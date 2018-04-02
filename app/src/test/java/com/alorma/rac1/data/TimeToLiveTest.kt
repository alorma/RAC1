package com.alorma.rac1.data;

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before;
import org.junit.Test
import java.util.concurrent.TimeUnit

class TimeToLiveTest {

    private lateinit var timeProvider: TimeProvider

    private lateinit var ttl: TimeToLive

    @Before
    fun setup() {
        timeProvider = mock()
    }

    @Test
    fun given10SecondsTime_withMoreTime_returnsFalse() {
        given(timeProvider.now()).willReturn(15)
        val ttl = TimeToLive(timeProvider, 5, TimeUnit.MILLISECONDS)

        val result = ttl.isValid(5)

        assertFalse(result)
    }

    @Test
    fun given20SecondsTime_withLessTime_returnsTrue() {
        given(timeProvider.now()).willReturn(15)
        val ttl = TimeToLive(timeProvider, 30, TimeUnit.MILLISECONDS)

        val result = ttl.isValid(5)

        assertTrue(result)
    }

    @Test
    fun given10SecondsTime_with10SecondsTime_returnsFalse() {
        given(timeProvider.now()).willReturn(15)
        val ttl = TimeToLive(timeProvider, 10, TimeUnit.MILLISECONDS)

        val result = ttl.isValid(5)

        assertFalse(result)
    }

}