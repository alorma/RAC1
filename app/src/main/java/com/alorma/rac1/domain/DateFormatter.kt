package com.alorma.rac1.domain

import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class DateFormatter @Inject constructor() {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss XXX"
    }

    // 2018-04-02T22:30:00+02:00
    fun mapDate(date: String): LocalDateTime {
        return OffsetDateTime.parse(date.replace("+", " +"), DateTimeFormatter.ofPattern(DATE_FORMAT)).toLocalDateTime()
    }

    fun mapDuration(duration: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}