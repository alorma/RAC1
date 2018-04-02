package com.alorma.rac1.data

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimeToLive @Inject constructor(
        private val timeProvider: TimeProvider,
        private val duration: Long,
        private val unit: TimeUnit) {

    fun isValid(time: Long): Boolean = (timeProvider.now() - time) < unit.toMillis(duration)
}