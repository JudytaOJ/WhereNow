package com.example.wherenow.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "dd LLLL yyyy"

fun Long.convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
    return formatter.format(Date(millis))
}

fun LocalDate.convertLocalDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy").withLocale(Locale.US)
    return LocalDate.now().format(formatter)
}