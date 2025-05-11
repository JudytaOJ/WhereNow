package com.example.wherenow.ui.app.settingsmenu.statesvisited

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.R
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.data.usecases.SaveStatesVisitedUseCase
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StateItem
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesVisitedUiIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class StatedVisitedViewModel(
    private val saveStatesVisitedUseCase: SaveStatesVisitedUseCase,
    private val getStatesVisitedUseCase: GetStatesVisitedUseCase
) : ViewModel() {

    private val _navigationEvents = Channel<StatedVisitedNavigationEvent>(capacity = Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _uiState = MutableStateFlow(StatedVisitedViewState())
    val uiState: StateFlow<StatedVisitedViewState> = _uiState.asStateFlow()

    internal fun onUiIntent(uiIntent: StatesVisitedUiIntent) {
        when (uiIntent) {
            StatesVisitedUiIntent.OnBackClicked -> _navigationEvents.trySend(StatedVisitedNavigationEvent.OnBackNavigation)
            is StatesVisitedUiIntent.OnCheckboxToggled -> onCheckboxToggled(uiIntent.id, uiIntent.isChecked)
        }
    }

    @VisibleForTesting
    internal fun loadData(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    statesList = getStatesVisitedUseCase.invoke(stateList(context = context))
                )
            }
        }
    }

    private fun onCheckboxToggled(id: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedList = _uiState.value.statesList.map {
                if (it.id == id) it.copy(isChecked = isChecked) else it
            }
            _uiState.value = _uiState.value.copy(statesList = updatedList)
            saveStatesVisitedUseCase.invoke(updatedList)
        }
    }

    private fun stateList(context: Context) = listOf<StateItem>(
        StateItem(context.getString(R.string.state_alabama), R.drawable.flag_of_alabama, 1),
        StateItem(context.getString(R.string.state_alaska), R.drawable.flag_of_alaska, 2),
        StateItem(context.getString(R.string.state_arizona), R.drawable.flag_of_arizona, 3),
        StateItem(context.getString(R.string.state_arkansas), R.drawable.flag_of_arkansas, 4),
        StateItem(context.getString(R.string.state_california), R.drawable.flag_of_california, 5),
        StateItem(context.getString(R.string.state_colorado), R.drawable.flag_of_colorado, 6),
        StateItem(context.getString(R.string.state_connecticut), R.drawable.flag_of_connecticut, 7),
        StateItem(context.getString(R.string.state_delaware), R.drawable.flag_of_delaware, 8),
        StateItem(context.getString(R.string.state_florida), R.drawable.flag_of_florida, 9),
        StateItem(context.getString(R.string.state_georgia), R.drawable.flag_of_georgia, 10),
        StateItem(context.getString(R.string.state_hawaii), R.drawable.flag_of_hawaii, 11),
        StateItem(context.getString(R.string.state_idaho), R.drawable.flag_of_idaho, 12),
        StateItem(context.getString(R.string.state_illinois), R.drawable.flag_of_illinois, 13),
        StateItem(context.getString(R.string.state_indiana), R.drawable.flag_of_indiana, 14),
        StateItem(context.getString(R.string.state_iowa), R.drawable.flag_of_iowa, 15),
        StateItem(context.getString(R.string.state_kansas), R.drawable.flag_of_kansas, 16),
        StateItem(context.getString(R.string.state_kentucky), R.drawable.flag_of_kentucky, 17),
        StateItem(context.getString(R.string.state_louisiana), R.drawable.flag_of_louisiana, 18),
        StateItem(context.getString(R.string.state_maine), R.drawable.flag_of_maine, 19),
        StateItem(context.getString(R.string.state_maryland), R.drawable.flag_of_maryland, 20),
        StateItem(context.getString(R.string.state_massachusetts), R.drawable.flag_of_massachusetts, 21),
        StateItem(context.getString(R.string.state_michigan), R.drawable.flag_of_michigan, 22),
        StateItem(context.getString(R.string.state_minnesota), R.drawable.flag_of_minnesota, 23),
        StateItem(context.getString(R.string.state_mississippi), R.drawable.flag_of_mississippi, 24),
        StateItem(context.getString(R.string.state_missouri), R.drawable.flag_of_missouri, 25),
        StateItem(context.getString(R.string.state_montana), R.drawable.flag_of_montana, 26),
        StateItem(context.getString(R.string.state_nebraska), R.drawable.flag_of_nebraska, 27),
        StateItem(context.getString(R.string.state_nevada), R.drawable.flag_of_nevada, 28),
        StateItem(context.getString(R.string.state_new_hampshire), R.drawable.flag_of_new_hampshire, 29),
        StateItem(context.getString(R.string.state_new_jersey), R.drawable.flag_of_new_jersey, 30),
        StateItem(context.getString(R.string.state_new_mexico), R.drawable.flag_of_new_mexico, 31),
        StateItem(context.getString(R.string.state_new_york), R.drawable.flag_of_new_york, 32),
        StateItem(context.getString(R.string.state_north_carolina), R.drawable.flag_of_north_carolina, 33),
        StateItem(context.getString(R.string.state_north_dakota), R.drawable.flag_of_north_dakota, 34),
        StateItem(context.getString(R.string.state_ohio), R.drawable.flag_of_ohio, 35),
        StateItem(context.getString(R.string.state_oklahoma), R.drawable.flag_of_oklahoma, 36),
        StateItem(context.getString(R.string.state_oregon), R.drawable.flag_of_oregon, 37),
        StateItem(context.getString(R.string.state_pennsylvania), R.drawable.flag_of_pennsylvania, 38),
        StateItem(context.getString(R.string.state_rhode_island), R.drawable.flag_of_rhode_island, 39),
        StateItem(context.getString(R.string.state_south_carolina), R.drawable.flag_of_south_carolina, 40),
        StateItem(context.getString(R.string.state_south_dakota), R.drawable.flag_of_south_dakota, 41),
        StateItem(context.getString(R.string.state_tennessee), R.drawable.flag_of_tennessee, 42),
        StateItem(context.getString(R.string.state_texas), R.drawable.flag_of_texas, 43),
        StateItem(context.getString(R.string.state_utah), R.drawable.flag_of_utah, 44),
        StateItem(context.getString(R.string.state_vermont), R.drawable.flag_of_vermont, 45),
        StateItem(context.getString(R.string.state_virginia), R.drawable.flag_of_virginia, 46),
        StateItem(context.getString(R.string.state_washington), R.drawable.flag_of_washington, 47),
        StateItem(context.getString(R.string.state_west_virginia), R.drawable.flag_of_west_virginia, 48),
        StateItem(context.getString(R.string.state_wisconsin), R.drawable.flag_of_wisconsin, 49),
        StateItem(context.getString(R.string.state_wyoming), R.drawable.flag_of_wyoming, 50)
    )
}