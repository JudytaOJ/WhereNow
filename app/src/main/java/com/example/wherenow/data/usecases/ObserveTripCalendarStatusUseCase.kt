package com.example.wherenow.data.usecases

import com.example.wherenow.repository.calendar.CalendarFlightRepository
import kotlinx.coroutines.flow.Flow

class ObserveTripCalendarStatusUseCase(
    private val repository: CalendarFlightRepository
) {
    operator fun invoke(tripId: Int): Flow<Boolean> {
        return repository.observeTripAddedToCalendar(tripId)
    }
}