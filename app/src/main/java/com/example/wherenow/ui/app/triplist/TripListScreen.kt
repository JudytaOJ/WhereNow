package com.example.wherenow.ui.app.triplist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.wherenow.database.Trip
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triplist.model.TripListUiIntent
import com.example.wherenow.ui.app.triplist.model.TripListViewState
import com.example.wherenow.ui.components.WhereNowDetailsTile
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowTextField
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import java.time.LocalDate

val SIZE_EMPTY_STATE_ANIMATION = 350.dp
val TONAL_ELEVATION = 72.dp
const val NAVIGATION_EVENTS_KEY = "NavigationEvents"

@Composable
internal fun TripListScreen(
    navigationEvent: (TripListNavigationEvent) -> Unit
) {
    val viewModel: TripListViewModel = hiltViewModel()
    TripList(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_EVENTS_KEY) {
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
                onChangeAppMode = { uiIntent(TripListUiIntent.OnChangeMode) },
                isArrowVisible = false,
                isModeVisible = true
            )
        }
    ) { padding ->
        if (state.tripList.isEmpty()) {
            EmptyStateList()
        } else {
            Column(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
                TripListContent(
                    state = state,
                    uiIntent = uiIntent
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
            WhereNowFloatingActionButton(onClick = { uiIntent(TripListUiIntent.OnAddTrip) })
        }
        TripListModalWithDetails(
            state = state,
            uiIntent = uiIntent
        )
    }
}

@Composable
private fun TripListContent(
    state: TripListViewState,
    uiIntent: (TripListUiIntent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(MaterialTheme.whereNowSpacing.space16)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space32),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = state.tripList, key = { id -> id.id }) { list ->
            WhereNowDetailsTile(
                city = list.departureCity,
                country = list.departureCountry,
                date = list.date,
                timeTravel = LocalDate.now(),
                countDays = 0,
                onDeleteClick = { uiIntent(TripListUiIntent.OnDeleteTrip(list.id)) },
                onClick = { uiIntent(TripListUiIntent.ShowTripDetails(list.id)) }
            )
        }
    }
}

@Composable
private fun EmptyStateList(
    modifier: Modifier = Modifier
) {
    val emptyAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_state_trip_list))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TripListModalWithDetails(
    state: TripListViewState,
    uiIntent: (TripListUiIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (state.detailsId != null) {
        ModalBottomSheet(
            onDismissRequest = { uiIntent(TripListUiIntent.HideTripDetails) },
            sheetState = sheetState,
            tonalElevation = TONAL_ELEVATION,
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = MaterialTheme.whereNowSpacing.space24)
                    .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.trip_modal_departure),
                    style = MaterialTheme.typography.titleLarge.copy(MaterialTheme.colorScheme.primary)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.whereNowSpacing.space16,
                        horizontal = MaterialTheme.whereNowSpacing.space8
                    )
                )

                WhereNowTextField(
                    label = stringResource(R.string.trip_details_city_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalCity ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_country_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalCountry ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_modal_departure_date),
                    value = state.tripList.find { it.id == state.detailsId }?.date ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_code_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalCodeAirport ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_airport_name_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalAirport ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space16))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.trip_modal_arrival),
                    style = MaterialTheme.typography.titleLarge.copy(MaterialTheme.colorScheme.primary)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.whereNowSpacing.space16,
                        horizontal = MaterialTheme.whereNowSpacing.space8
                    )
                )
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_city_label),
                    value = state.tripList.find { it.id == state.detailsId }?.departureCity ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_country_label),
                    value = state.tripList.find { it.id == state.detailsId }?.departureCountry ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_code_label),
                    value = state.tripList.find { it.id == state.detailsId }?.departureCodeAirport ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_airport_name_label),
                    value = state.tripList.find { it.id == state.detailsId }?.departureAirport ?: StringUtils.EMPTY
                )
            }
        }
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
        tripList = listOf(
            Trip(
                date = "23.12.2024",
                departureCity = "Warsaw",
                departureCountry = "Poland",
                departureAirport = "Chopin Warsaw",
                departureCodeAirport = "WAW",
                arrivalCity = "Berlin",
                arrivalCountry = "Germany",
                arrivalAirport = "Berlin Brandenburg Airport",
                arrivalCodeAirport = "BER",
                id = 1
            ),
            Trip(
                date = "12.06.2024",
                departureCity = "Athens",
                departureCountry = "Greece",
                departureAirport = "Athens International Airport",
                departureCodeAirport = "ATH",
                arrivalCity = "Istanbul",
                arrivalCountry = "Turkey",
                arrivalAirport = "Istanbul Airport",
                arrivalCodeAirport = "IST",
                id = 2
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