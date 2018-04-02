package com.alorma.rac1.domain

import org.threeten.bp.Duration

fun Duration.asMinutes(): Long {
    return minusHours(toHours()).toMinutes()
}
