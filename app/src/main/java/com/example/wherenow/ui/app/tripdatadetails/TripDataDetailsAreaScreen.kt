package com.example.wherenow.ui.app.tripdatadetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsViewState
import com.example.wherenow.ui.components.WhereNowOutlinedTextField
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

val LOTTIE_HEIGHT = 150.dp

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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownFromCityScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                readOnly = true
            )

            ExposedDropdownMenu(
                modifier = Modifier.onFocusChanged { focusManager.clearFocus() },
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                state.cityList.forEach { city ->
                    DropdownMenuItem(
                        modifier = Modifier.semantics {
                            contentDescription = "Departure city list"
                            role = Role.DropdownList
                        },
                        text = {
                            Text(
                                text = city.attributes.city,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCity(city.attributes.city))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureCountry(city.attributes.country))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportName(city.attributes.name))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateDepartureAirportCode(city.attributes.iata))
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        colors = MenuItemColors(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.background,
                            trailingIconColor = MaterialTheme.colorScheme.errorContainer,
                            disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }
        if (state.isErrorDepartureCity) {
            ErrorText()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownToCityScreen(
    modifier: Modifier,
    state: TripDataDetailsViewState,
    uiIntent: (TripDataDetailsUiIntent) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .onFocusChanged { focusManager.clearFocus() }
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                value = state.departureCity,
                onValueChange = { uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(state.arrivalCity)) },
                label = {
                    Text(
                        text = stringResource(R.string.trip_details_city_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                readOnly = true
            )

            ExposedDropdownMenu(
                modifier = Modifier.onFocusChanged { focusManager.clearFocus() },
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                state.cityList.forEach { city ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Arrival city list"
                                role = Role.DropdownList
                            },
                        text = {
                            Text(
                                text = city.attributes.city,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCity(city.attributes.city))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalCountry(city.attributes.country))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportName(city.attributes.name))
                            uiIntent(TripDataDetailsUiIntent.OnUpdateArrivalAirportCode(city.attributes.iata))
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        colors = MenuItemColors(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.background,
                            trailingIconColor = MaterialTheme.colorScheme.errorContainer,
                            disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
            }
        }
        if (state.isErrorArrivalCity) {
            ErrorText()
        }
    }
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
        departureCity = "Warszawa",
        arrivalCity = "Gdańsk",
        arrivalCodeAirport = "WAW",
        departureCodeAirport = "GDA",
        arrivalAirport = "Chopin",
        departureAirport = "Wałęsa",
        arrivalCountry = "Polska",
        departureCountry = "Polska"
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