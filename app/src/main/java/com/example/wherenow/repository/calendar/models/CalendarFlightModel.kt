package com.example.wherenow.repository.calendar.models

data class CalendarFlightModel(
    val title: String,
    val description: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long
)