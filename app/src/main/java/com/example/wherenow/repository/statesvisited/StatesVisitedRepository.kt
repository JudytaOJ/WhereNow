package com.example.wherenow.repository.statesvisited

import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem

interface StatesVisitedRepository {
    suspend fun saveStatesVisitedList(list: List<StateItem>)
    suspend fun getStatesVisitedList(stateList: List<StateItem>): List<StateItem>
}

class StatesVisitedRepositoryImpl(
    private val statesVisitedDataStore: StatesVisitedDataStore
) : StatesVisitedRepository {

    override suspend fun saveStatesVisitedList(list: List<StateItem>) {
        list.forEach { item ->
            statesVisitedDataStore.saveStateVisited(item.id, item.isChecked)
        }
    }

    override suspend fun getStatesVisitedList(stateList: List<StateItem>): List<StateItem> {
        return statesVisitedDataStore.getAllVisitedStates(stateList)
    }
}