package com.example.wherenow.ui.app.settingsmenu.appTheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.usecases.GetThemeModeUseCase
import com.example.wherenow.data.usecases.SetThemeModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getThemeModeUseCase().collect { _isDarkTheme.value = it }
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            runCatching {
                val newValue = !_isDarkTheme.value
                setThemeModeUseCase(newValue)
            }
        }
    }
}