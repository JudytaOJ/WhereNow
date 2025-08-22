package com.example.wherenow.repository.calendar.models

data class CalendarFlight(
    val title: String,
    val description: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long
)

fun CalendarFlightModel.toCalendarModel(): CalendarFlight = CalendarFlight(
    title = title,
    description = description,
    startTimeMillis = startTimeMillis,
    endTimeMillis = endTimeMillis
)