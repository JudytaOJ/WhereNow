package com.example.wherenow.ui.app.triptiledetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.app.triplist.TONAL_ELEVATION
import com.example.wherenow.ui.app.triplist.TRIP_MODAL_MAX_HEIGHT
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsUiIntent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsViewState
import com.example.wherenow.ui.components.WhereNowDetailsFlightTile
import com.example.wherenow.ui.components.WhereNowProgressBar
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.components.textfield.WhereNowTextField
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.convertLongToTime
import com.example.wherenow.util.textWithFirstUppercaseChar
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_TILE_DETAILS_KEY = "NavigationTileDetailsKey"

@Composable
internal fun TripTileDetailsScreen(
    navigationEvent: (TripTileDetailsNavigationEvent) -> Unit
) {
    val viewModel: TripTileDetailsViewModel = koinViewModel()
    TripTileDetails(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_TILE_DETAILS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun TripTileDetails(
    state: TripTileDetailsViewState,
    uiIntent: (TripTileDetailsUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.app_name),
                onBackAction = { uiIntent(TripTileDetailsUiIntent.OnBackClicked) }
            )
        },
    ) { padding ->
        if (state.isLoading) {
            WhereNowProgressBar()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(
                        horizontal = MaterialTheme.whereNowSpacing.space24,
                        vertical = MaterialTheme.whereNowSpacing.space32
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                WhereNowDetailsFlightTile(
                    cardDescription = stringResource(R.string.trip_details_tile_list_name_flight_details),
                    cardSupportedText = stringResource(R.string.trip_details_supported_text_flight_details),
                    image = painterResource(R.drawable.flight_icon),
                    onClick = { uiIntent(TripTileDetailsUiIntent.ShowTripDetails) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.whereNowSpacing.space24))
                WhereNowDetailsFlightTile(
                    cardDescription = stringResource(R.string.trip_details_tile_list_name_important_notes),
                    cardSupportedText = stringResource(R.string.trip_details_supported_text_important_notes),
                    image = painterResource(R.drawable.notes_icon),
                    onClick = { uiIntent(TripTileDetailsUiIntent.ImportantNotesDetails(state.detailsId ?: 0)) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.whereNowSpacing.space24))
                WhereNowDetailsFlightTile(
                    cardDescription = stringResource(R.string.trip_details_tile_list_name_add_file),
                    cardSupportedText = stringResource(R.string.trip_details_supported_text_list_name_add_file),
                    image = painterResource(R.drawable.files_icon),
                    onClick = { uiIntent(TripTileDetailsUiIntent.AddFiles(state.detailsId ?: 0)) }
                )
            }
        }
    }
    ModalWithDetailsFlight(
        state = state,
        uiIntent = uiIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalWithDetailsFlight(
    state: TripTileDetailsViewState,
    uiIntent: (TripTileDetailsUiIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    if (state.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { uiIntent(TripTileDetailsUiIntent.HideTripDetails) },
            sheetState = sheetState,
            tonalElevation = TONAL_ELEVATION,
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = screenHeight * TRIP_MODAL_MAX_HEIGHT)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = MaterialTheme.whereNowSpacing.space24)
                    .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .semantics { heading() }
                            .padding(end = MaterialTheme.whereNowSpacing.space8),
                        text = stringResource(R.string.trip_details_departure),
                        style = MaterialTheme.typography.titleLarge.copy(MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Icon(
                        painter = painterResource(R.drawable.departures_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.whereNowSpacing.space16,
                        horizontal = MaterialTheme.whereNowSpacing.space8
                    )
                )
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_city_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalCity?.textWithFirstUppercaseChar() ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_country_label),
                    value = state.tripList.find { it.id == state.detailsId }?.arrivalCountry?.textWithFirstUppercaseChar() ?: StringUtils.EMPTY
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space4))
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_date),
                    value = convertLongToTime(state.tripList.find { it.id == state.detailsId }?.date ?: 0)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .semantics { heading() }
                            .padding(end = MaterialTheme.whereNowSpacing.space8),
                        text = stringResource(R.string.trip_details_arrival),
                        style = MaterialTheme.typography.titleLarge.copy(MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Icon(
                        painter = painterResource(R.drawable.arrivals_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
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

                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space16))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .semantics { heading() }
                            .padding(end = MaterialTheme.whereNowSpacing.space8),
                        text = stringResource(R.string.trip_details_distance_label),
                        style = MaterialTheme.typography.titleLarge.copy(MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Icon(
                        painter = painterResource(R.drawable.distance_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.whereNowSpacing.space16,
                        horizontal = MaterialTheme.whereNowSpacing.space8
                    )
                )
                WhereNowTextField(
                    label = stringResource(R.string.trip_details_distance_between_cites),
                    value = buildString {
                        append(state.tripList.find { it.id == state.detailsId }?.distance ?: StringUtils.EMPTY)
                        append(StringUtils.SPACE)
                        append(stringResource(R.string.trip_details_miles))
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TripTileDetailsPreview() {
    WhereNowTheme {
        TripTileDetails(
            state = TripTileDetailsViewState(
                isLoading = false
            ),
            uiIntent = {}
        )
    }
}