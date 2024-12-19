package com.example.wherenow.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "dd MMMM yyyy"

fun Long.convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return formatter.format(Date(millis))
}