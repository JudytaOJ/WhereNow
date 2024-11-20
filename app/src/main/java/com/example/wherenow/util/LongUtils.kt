package com.example.wherenow.util

import com.example.wherenow.ui.components.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    return formatter.format(Date(millis))
}