package com.example.wherenow.data.usecases

import com.example.wherenow.repository.statesvisited.StatesVisitedRepository
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem

class SaveStatesVisitedUseCase internal constructor(
    private val statesVisitedRepository: StatesVisitedRepository
) {
    suspend operator fun invoke(newValue: List<StateItem>) =
        statesVisitedRepository.saveStatesVisitedList(newValue)
}