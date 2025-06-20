package com.example.wherenow.repository.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class ThemeDataStore(private val context: Context) {

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val themeFlow: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_MODE_KEY] == true }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
}