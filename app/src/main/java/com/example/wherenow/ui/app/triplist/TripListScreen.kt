package com.example.wherenow.ui.app.triplist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.wherenow.database.trip.Trip
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.app.triplist.model.TripListViewState
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowProgressBar
import com.example.wherenow.ui.components.WhereNowSegmentedButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTile
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import kotlinx.collections.immutable.persistentListOf

val SIZE_EMPTY_STATE_ANIMATION = 350.dp
val TONAL_ELEVATION = 72.dp
const val TRIP_MODAL_MAX_HEIGHT = 0.93f
const val NAVIGATION_LIST_KEY = "NavigationList"

@Composable
internal fun TripListScreen(
    navigationEvent: (TripListNavigationEvent) -> Unit
) {
    val viewModel: TripListViewModel = hiltViewModel()
    TripList(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_LIST_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun TripList(
    state: TripListViewState,
    uiIntent: (TripListUiIntent) -> Unit
) {
    BackHandler(true) { /*do nothing*/ }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.app_name),
                onCloseApp = { uiIntent(TripListUiIntent.OnCloseApp) },
                isArrowVisible = false,
                isCloseAppIconVisible = true
            )
        },
        floatingActionButton = {
            WhereNowFloatingActionButton(onClick = { uiIntent(TripListUiIntent.OnAddTrip) })
        },
    ) { padding ->
        when {
            state.isLoading -> WhereNowProgressBar()
            state.tripList.isEmpty() -> {
                Column(modifier = Modifier.padding(padding)) {
                    Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                    EmptyStateList(
                        state = state,
                        uiIntent = uiIntent
                    )
                }
            }

            else -> {
                Column(modifier = Modifier.padding(padding)) {
                    Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
                    WhereNowSegmentedButton(
                        options = state.optionsList,
                        onSelectedIndexClick = { uiIntent(TripListUiIntent.OnGetListDependsButtonType(it)) },
                        selectedButtonType = state.selectedButtonType
                    )
                    TripListContent(
                        state = state,
                        uiIntent = uiIntent
                    )
                }
            }
        }
    }
}

@Composable
private fun TripListContent(
    state: TripListViewState,
    uiIntent: (TripListUiIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.whereNowSpacing.space16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space32),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = state.tripList,
            key = { id -> id.id }
        ) { list ->
            WhereNowDetailsTile(
                city = list.departureCity,
                country = list.departureCountry,
                date = list.date,
                timeTravel = list.date.takeLast(4).toInt(),
                onDeleteClick = { uiIntent(TripListUiIntent.OnDeleteTrip(list.id, state.selectedButtonType)) },
                onClick = { uiIntent(TripListUiIntent.ShowTripDetails(list.id)) },
                image = list.image
            )
        }
    }
}

@Composable
private fun EmptyStateList(
    modifier: Modifier = Modifier,
    state: TripListViewState,
    uiIntent: (TripListUiIntent) -> Unit
) {
    val emptyAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.empty_state_trip_list)
    )
    val emptyAnimationProgress by animateLottieCompositionAsState(
        composition = emptyAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.whereNowSpacing.space16),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.whereNowSpacing.space8))
        WhereNowSegmentedButton(
            options = state.optionsList,
            onSelectedIndexClick = { uiIntent(TripListUiIntent.OnGetListDependsButtonType(it)) },
            selectedButtonType = state.selectedButtonType
        )
        LottieAnimation(
            composition = emptyAnimation,
            progress = emptyAnimationProgress,
            alignment = Alignment.BottomCenter,
            modifier = Modifier.size(SIZE_EMPTY_STATE_ANIMATION)
        )
        Text(
            modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space32),
            text = stringResource(R.string.trip_list_empty_state),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun TripEmptyListPreview() {
    val state = TripListViewState()
    WhereNowTheme {
        TripList(
            state = state,
            uiIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun TripListPreview() {
    val state = TripListViewState(
        tripList = persistentListOf(
            Trip(
                date = "23.12.2024",
                image = WhereNowDetailsTileImageType.US_HAWAII.icon,
                departureCity = "New York",
                departureCountry = "United States",
                departureAirport = "John F. Kennedy International Airport",
                departureCodeAirport = "JFK",
                arrivalCity = "Los Angeles",
                arrivalCountry = "United States",
                arrivalAirport = "Los Angeles International Airport",
                arrivalCodeAirport = "LAX",
                id = 1,
                distance = "1234"
            ),
            Trip(
                date = "12.06.2024",
                image = WhereNowDetailsTileImageType.US_MONUMENT_VALLEY.icon,
                departureCity = "San Francisco",
                departureCountry = "United States",
                departureAirport = "San Francisco International Airport",
                departureCodeAirport = "SFO",
                arrivalCity = "Seattle",
                arrivalCountry = "United States",
                arrivalAirport = "Seattle-Tacoma International Airport",
                arrivalCodeAirport = "SEA",
                id = 2,
                distance = "1234"
            )
        )
    )
    WhereNowTheme {
        TripList(
            state = state,
            uiIntent = {}
        )
    }
}