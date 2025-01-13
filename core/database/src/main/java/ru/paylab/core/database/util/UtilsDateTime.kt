package ru.paylab.core.database.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

object UtilsDateTime {
    private val pubDateFormats = listOf(
        "EEE, dd MMM yyyy HH:mm:ss zzz",
        "EEE, dd MMM yyyy HH:mm zzz",
        "EEE, dd MMM yyyy HH:mm zzz",
    )
    private val pubDateFormats2 = listOf(
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.BASIC_ISO_DATE,
        DateTimeFormatter.ISO_LOCAL_DATE,
        DateTimeFormatter.ISO_OFFSET_DATE,
        DateTimeFormatter.ISO_DATE,
        DateTimeFormatter.ISO_LOCAL_TIME,
        DateTimeFormatter.ISO_OFFSET_TIME,
        DateTimeFormatter.ISO_TIME,
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ISO_ORDINAL_DATE,
        DateTimeFormatter.ISO_WEEK_DATE,
        DateTimeFormatter.ISO_INSTANT,
    )

    fun parsePubDateStringToLong(value: String) = parsePubDateStringToDate(value)

    private fun parsePubDateStringToDate(value: String): Long {
        for (dateFormat in pubDateFormats) {
            try {
                LocalDate.parse(value)
                val articleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
                val longVal = articleDateFormat.parse(value)!!.time
                println("longVal0 $longVal")
                return longVal
            } catch (_: Throwable) {
            }
        }
        for (dateFormat in pubDateFormats2) {
            try {
                val longVal = LocalDateTime.parse(value, dateFormat).toEpochSecond(ZoneOffset.UTC)
                println("longVal1 $longVal")
                return longVal
            } catch (_: DateTimeParseException) {
            }
        }
        return Date().time
    }

}