package com.example.wherenow.ui.triplist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.components.WhereNowDetailsTile
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.ui.triplist.model.WhereNowTripListNavigationEvent
import com.example.wherenow.ui.triplist.model.WhereNowTripListUiIntent
import java.time.LocalDate

val SIZE_EMPTY_STATE_ANIMATION = 300.dp
const val NAVIGATION_EVENTS_KEY = "NavigationEvents"

@Composable
internal fun WhereNowListTrip(
    navigationEvent: (WhereNowTripListNavigationEvent) -> Unit
) {
    val viewModel: WhereNowTripListViewModel = hiltViewModel()
    WhereNowTripList(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_EVENTS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun WhereNowTripList(
    state: WhereNowTripListViewState,
    uiIntent: (WhereNowTripListUiIntent) -> Unit
) {
    BackHandler { null }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.app_name),
                onChangeAppMode = { uiIntent(WhereNowTripListUiIntent.OnChangeMode) },
                isArrowVisible = false,
                isModeVisible = true
            )
        }
    ) { padding ->
        if (state.tripList.isEmpty()) {
            WhereNowEmptyStateList()
        } else {
            Column(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
                WhereNowTripListContent(
                    state = state
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.whereNowSpacing.space24),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            WhereNowFloatingActionButton(onClick = { uiIntent(WhereNowTripListUiIntent.OnAddTrip) })
        }
    }
}

@Composable
private fun WhereNowTripListContent(
    state: WhereNowTripListViewState
) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.whereNowSpacing.space16)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space32),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.tripList.forEach {
            WhereNowDetailsTile(
                city = state.cityName,
                country = state.countryName,
                date = "test",
                timeTravel = LocalDate.now(),
                countDays = 0,
                onClick = {}
            )
        }
    }
}

@Composable
private fun WhereNowEmptyStateList(
    modifier: Modifier = Modifier
) {
    val emptyAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.trip_list_empty_state))
    val emptyAnimationProgress by animateLottieCompositionAsState(
        composition = emptyAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.whereNowSpacing.space16),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = emptyAnimation,
            progress = emptyAnimationProgress,
            alignment = Alignment.BottomCenter,
            modifier = Modifier.size(SIZE_EMPTY_STATE_ANIMATION)
        )
        Text(
            modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space24),
            text = stringResource(R.string.trip_list_empty_state),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun WhereNowTripListPreview() {
    val state = WhereNowTripListViewState()
    WhereNowTheme {
        WhereNowTripList(
            state = state,
            uiIntent = {}
        )
    }
}