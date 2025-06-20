package com.example.wherenow.repository.theme

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val isDarkMode: Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}

class ThemeRepositoryImpl(
    private val dataStore: ThemeDataStore
) : ThemeRepository {

    override val isDarkMode: Flow<Boolean> = dataStore.themeFlow

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.setDarkMode(enabled)
    }
}