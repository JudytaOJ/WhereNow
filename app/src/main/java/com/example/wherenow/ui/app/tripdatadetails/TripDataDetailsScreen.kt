package com.example.wherenow.ui.app.tripdatadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsViewState
import com.example.wherenow.ui.components.WhereNowButton
import com.example.wherenow.ui.components.WhereNowDataPicker
import com.example.wherenow.ui.components.WhereNowProgressBar
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_DATA_DETAILS_KEY = "NavigationDataDetailsEvents"

@Composable
internal fun TripDataDetailsScreen(
    navigationEvent: (TripDataDetailsNavigationEvent) -> Unit
) {
    val viewModel: TripDataDetailsViewModel = koinViewModel()
    TripDataDetailsContent(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_DATA_DETAILS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun TripDataDetailsContent(
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.trip_details_title),
                onBackAction = { uiIntent(TripDataDetailsUiIntent.OnBackNavigation) }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            WhereNowProgressBar()
        } else {
            Column(
                modifier = Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                        .verticalScroll(rememberScrollState())
                ) {
                    WhereNowDataPicker(
                        modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space16),
                        date = state.date,
                        onUpdateDate = { uiIntent(TripDataDetailsUiIntent.OnUpdateDate(it ?: 0)) }
                    )
                    TripDataDetailsAreaScreen(
                        modifier = Modifier,
                        state = state,
                        uiIntent = uiIntent
                    )
                    Spacer(Modifier.weight(1f))
                    WhereNowButton(
                        modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space16),
                        buttonText = stringResource(R.string.button_text_add),
                        onClick = { uiIntent(TripDataDetailsUiIntent.OnNextClicked) }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TripDataDetailsPreview() {
    val state = TripDataDetailsViewState()
    WhereNowTheme {
        TripDataDetailsContent(
            state = state,
            uiIntent = {}
        )
    }
}