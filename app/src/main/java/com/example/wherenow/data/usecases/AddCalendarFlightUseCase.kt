package com.example.wherenow.data.usecases

import com.example.wherenow.repository.calendar.CalendarFlightRepository
import com.example.wherenow.repository.calendar.models.CalendarFlightModel
import com.example.wherenow.repository.calendar.models.toCalendarModel

class AddCalendarFlightUseCase(
    private val repository: CalendarFlightRepository
) {
    operator fun invoke(model: CalendarFlightModel): Boolean {
        val calendarId = repository.getCalendarId() ?: return false
        return repository.addEvent(model.toCalendarModel(), calendarId)
    }
}