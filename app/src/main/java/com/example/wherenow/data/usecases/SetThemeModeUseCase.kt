package com.example.wherenow.data.usecases

import com.example.wherenow.repository.theme.ThemeRepository

class SetThemeModeUseCase(
    private val themeRepository: ThemeRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        themeRepository.setDarkMode(enabled)
    }
}