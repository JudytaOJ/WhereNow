package com.example.wherenow.util

import okhttp3.internal.UTC
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val DATE_FORMAT = "dd LLLL yyyy"

fun LocalDate.convertLocalDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy").withLocale(Locale.US)
    return LocalDate.now().format(formatter)
}

fun convertLocalDateToTimestampUTC(localDate: LocalDate): Long {
    val zonedDateTime = localDate.atStartOfDay(UTC.toZoneId())
    val instant = zonedDateTime.toInstant()
    return instant.toEpochMilli()
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat(DATE_FORMAT, Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(date)
}