package com.example.wherenow.data.usecases

import com.example.wherenow.repository.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetThemeModeUseCase(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Flow<Boolean> = themeRepository.isDarkMode
}