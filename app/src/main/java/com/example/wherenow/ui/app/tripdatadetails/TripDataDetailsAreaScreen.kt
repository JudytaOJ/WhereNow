package com.example.wherenow.ui.app.tripdatadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.data.dto.DataItemDto
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsViewState
import com.example.wherenow.ui.components.textfield.WhereNowOutlinedTextField
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils

val LOTTIE_HEIGHT = 150.dp
val TONAL_ELEVATION_DP = 72.dp
const val CITY_MODAL_MAX_HEIGHT = 0.93f

@Composable
internal fun TripDataDetailsAreaScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    DropdownScreen(
        modifier = modifier,
        state = state,
        uiIntent = uiIntent
    )
}

@Composable
private fun DropdownScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    val tripDetailsAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.trip_details_animation)
    )

    LottieAnimation(
        composition = tripDetailsAnimation,
        modifier = Modifier
            .fillMaxWidth()
            .height(LOTTIE_HEIGHT)
    )

    Column {
        Row {
            DropdownFromCityScreen(
                modifier = modifier.weight(1f),
                state = state,
                uiIntent = uiIntent
            )
            Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space8))
            DropdownToCityScreen(
                modifier = modifier.weight(1f),
                state = state,
                uiIntent = uiIntent
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space16)
        )
        Row {
            FromDropdownArea(
                modifier = modifier.weight(1f),
                state = state,
                uiIntent = uiIntent
            )
            Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space8))
            ToDropdownArea(
                modifier = modifier.weight(1f),
                state = state,
                uiIntent = uiIntent
            )
        }
        if (state.arrivalCity.isNotEmpty() && state.departureCity.isNotEmpty()) {
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = buildString {
                    append(state.distance)
                    append(StringUtils.SPACE)
                    append(stringResource(R.string.trip_details_miles))
                },
                onClick = {},
                label = stringResource(R.string.trip_details_distance_label),
                readOnly = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownFromCityScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { uiIntent(TripDataDetailsUiIntent.ShowModalFromCityList) }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .clickable { uiIntent(TripDataDetailsUiIntent.ShowModalFromCityList) }
                    .onFocusChanged { focusManager.clearFocus() },
                value = state.arrivalCity,
                onValueChange = { uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(state.arrivalCity)) },
                label = {
                    Text(
                        text = stringResource(R.string.trip_details_city_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                readOnly = true
            )
        }
        if (state.isErrorDepartureCity) {
            ErrorText()
        }
        ModalWithFromCityList(
            state = state,
            uiIntent = uiIntent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalWithFromCityList(
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    if (state.showBottomSheetFromCityList) {
        ModalBottomSheet(
            onDismissRequest = { uiIntent(TripDataDetailsUiIntent.HideModalFromCityList) },
            sheetState = sheetState,
            tonalElevation = TONAL_ELEVATION_DP,
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = screenHeight * CITY_MODAL_MAX_HEIGHT)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = MaterialTheme.whereNowSpacing.space24)
                    .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.cityList,
                        key = { id -> id.id }
                    ) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(city.attributes.city))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCountry(city.attributes.country))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportName(city.attributes.name))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportCode(city.attributes.iata))
                                    uiIntent(TripDataDetailsUiIntent.HideModalFromCityList)
                                }
                        ) {
                            Column {
                                Text(
                                    text = city.attributes.city,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        vertical = MaterialTheme.whereNowSpacing.space16
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownToCityScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = { uiIntent(TripDataDetailsUiIntent.ShowModalToCityList) }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .onFocusChanged { focusManager.clearFocus() }
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .clickable { uiIntent(TripDataDetailsUiIntent.ShowModalToCityList) },
                value = state.departureCity,
                onValueChange = { uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(state.arrivalCity)) },
                label = {
                    Text(
                        text = stringResource(R.string.trip_details_city_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                readOnly = true
            )
            if (state.isErrorArrivalCity) {
                ErrorText()
            }
            ModalWithToCityList(
                state = state,
                uiIntent = uiIntent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalWithToCityList(
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    if (state.showBottomSheetToCityList) {
        ModalBottomSheet(
            onDismissRequest = { uiIntent(TripDataDetailsUiIntent.HideModalToCityList) },
            sheetState = sheetState,
            tonalElevation = TONAL_ELEVATION_DP,
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .heightIn(max = screenHeight * CITY_MODAL_MAX_HEIGHT)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = MaterialTheme.whereNowSpacing.space24)
                    .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.cityList,
                        key = { id -> id.id }
                    ) { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(city.attributes.city))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCountry(city.attributes.country))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportName(city.attributes.name))
                                    uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportCode(city.attributes.iata))
                                    uiIntent(TripDataDetailsUiIntent.HideModalToCityList)
                                }
                        ) {
                            Column {
                                Text(
                                    text = city.attributes.city,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        vertical = MaterialTheme.whereNowSpacing.space16
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.whereNowSpacing.space4,
                start = MaterialTheme.whereNowSpacing.space4
            )
            .semantics {
                liveRegion = LiveRegionMode.Assertive
                SemanticsProperties.Error
            },
        text = stringResource(R.string.trip_details_city_input_validation),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun FromDropdownArea(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (state.arrivalCity.isNotEmpty()) {
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.arrivalCodeAirport,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportCode(state.arrivalCodeAirport)) },
                label = stringResource(R.string.trip_details_code_label),
                readOnly = true
            )
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.arrivalCountry,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCountry(state.arrivalCountry)) },
                label = stringResource(R.string.trip_details_country_label),
                readOnly = true
            )
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.arrivalAirport,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportName(state.arrivalAirport)) },
                label = stringResource(R.string.trip_details_airport_name_label),
                readOnly = true
            )
        }
    }
}

@Composable
private fun ToDropdownArea(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (state.departureCity.isNotEmpty()) {
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.departureCodeAirport,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportCode(state.departureCodeAirport)) },
                label = stringResource(R.string.trip_details_code_label),
                readOnly = true
            )
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.departureCountry,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCountry(state.departureCountry)) },
                label = stringResource(R.string.trip_details_country_label),
                readOnly = true
            )
            WhereNowOutlinedTextField(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = state.departureAirport,
                onClick = { uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportName(state.departureAirport)) },
                label = stringResource(R.string.trip_details_airport_name_label),
                readOnly = true
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DropdownPreview() {
    val state = TripDataDetailsViewState(
        departureCity = "New York",
        arrivalCity = "Los Angeles",
        arrivalCodeAirport = "JFK",
        departureCodeAirport = "LAX",
        arrivalAirport = "Los Angeles International Airport",
        departureAirport = "John F. Kennedy International Airport",
        arrivalCountry = "United States",
        departureCountry = "United States",
        distance = "2475"
    )
    WhereNowTheme {
        TripDataDetailsAreaScreen(
            modifier = Modifier,
            state = state,
            uiIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun DropdownErrorPreview() {
    val state = TripDataDetailsViewState(
        isErrorDepartureCity = true,
        isErrorArrivalCity = true
    )
    WhereNowTheme {
        TripDataDetailsAreaScreen(
            modifier = Modifier,
            state = state,
            uiIntent = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun ModalWithFromCityListPreview() {
    WhereNowTheme {
        ModalWithFromCityList(
            state = TripDataDetailsViewState(
                showBottomSheetFromCityList = true,
                cityList = listOf(
                    AttributesDto(
                        attributes = DataItemDto(
                            city = "Miami",
                            country = "United States",
                            iata = "MIA",
                            icao = "KMIA",
                            name = "Miami International Airport"
                        ),
                        id = "1",
                        type = "1"
                    ),
                    AttributesDto(
                        attributes = DataItemDto(
                            city = "Miami",
                            country = "United States",
                            iata = "MIA",
                            icao = "KMIA",
                            name = "Miami International Airport"
                        ),
                        id = "2",
                        type = "2"
                    ),
                    AttributesDto(
                        attributes = DataItemDto(
                            city = "Miami",
                            country = "United States",
                            iata = "MIA",
                            icao = "KMIA",
                            name = "Miami International Airport"
                        ),
                        id = "3",
                        type = "3"
                    )
                )
            ),
            uiIntent = {}
        )
    }
}