package com.example.wherenow.repository.statesvisited

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import kotlinx.coroutines.flow.first

class StatesVisitedDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = NAME_KEY)

    private fun stateKey(id: Int) = booleanPreferencesKey("state_visited_$id")

    suspend fun saveStateVisited(id: Int, checked: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[stateKey(id)] = checked
        }
    }

    suspend fun getAllVisitedStates(stateList: List<StateItem>): List<StateItem> {
        val prefs = context.dataStore.data.first()
        return stateList.map { state ->
            val isChecked = prefs[stateKey(state.id)] == true
            state.copy(isChecked = isChecked)
        }
    }

    companion object {
        const val NAME_KEY = "states_visited"
    }
}