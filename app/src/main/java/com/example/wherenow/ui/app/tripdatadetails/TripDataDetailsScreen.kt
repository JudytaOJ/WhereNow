package com.example.wherenow.ui.app.tripdatadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.tripdatadetails.model.TripDataDetailsUiIntent
import com.example.wherenow.ui.app.triplist.NAVIGATION_EVENTS_KEY
import com.example.wherenow.ui.components.WhereNowButton
import com.example.wherenow.ui.components.WhereNowDropdown
import com.example.wherenow.ui.components.WhereNowOutlinedTextField
import com.example.wherenow.ui.components.WhereNowProgressBar
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@Composable
internal fun TripDataDetailsScreen(
    navigationEvent: (TripDataDetailsNavigationEvent) -> Unit
) {
    val viewModel: TripDataDetailsViewModel = hiltViewModel()
    TripDataDetailsContent(
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_EVENTS_KEY) {
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
                onBackAction = { uiIntent(TripDataDetailsUiIntent.OnBackNavigation) },
                onChangeAppMode = {},
                isArrowVisible = false,
                isModeVisible = true
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
                        .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                        .fillMaxSize()
                ) {
                    DateInput()
                    CityCodeArea()
                    DetailsArea()
                    DistanceArea(distance = "250 km")
                    Spacer(Modifier.weight(1f))
                    WhereNowButton(
                        modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space16),
                        buttonText = stringResource(R.string.button_text),
                        onClick = {/*TODO*/ }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateInput() {
    //TODO
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.whereNowSpacing.space24),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "25 sierpień 2015",
            style = MaterialTheme.typography.titleMedium.copy(MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}

@Composable
private fun CityCodeArea() {
    val tripDetailsAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.trip_details_animation)
    )

    LottieAnimation(
        composition = tripDetailsAnimation,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space24)
    ) {
        DropdownCityMenu(
            modifier = Modifier.weight(1f),
            label = "Wylot",
            cityName = "Wawka"
        )
        Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space8))
        DropdownCityMenu(
            modifier = Modifier.weight(1f),
            label = "Przylot",
            cityName = "Poznań"
        )
    }
}

@Composable
private fun DropdownCityMenu(
    modifier: Modifier,
    cityList: List<String> = listOf("Wawka", "Gdynia", "Zielona Góra"),
    cityName: String,
    label: String
) {
    WhereNowDropdown(
        cityName = cityName,
        cityList = cityList,
        modifier = modifier,
        label = label
    )
}

@Composable
private fun DetailsArea() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailsFlightArea(
            modifier = Modifier.weight(1f),
            code = "GDA",
            countryName = "Polska",
            airportName = "GDA"
        )
        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                .height(220.dp)
        )
        DetailsFlightArea(
            modifier = Modifier.weight(1f),
            code = "WAW",
            countryName = "Polska",
            airportName = "GDA"
        )
    }
}

@Composable
private fun DetailsFlightArea(
    modifier: Modifier = Modifier,
    code: String,
    countryName: String,
    airportName: String
) {
    val codeText = remember { mutableStateOf(code) }
    val country = remember { mutableStateOf(countryName) }
    val airport = remember { mutableStateOf(airportName) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        WhereNowOutlinedTextField(
            modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space8),
            text = codeText.value,
            placeholder = stringResource(R.string.trip_details_code_placeholder),
            label = stringResource(R.string.trip_details_departure)
        )
        WhereNowOutlinedTextField(
            modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space8),
            text = country.value,
            label = stringResource(R.string.trip_details_country_label),
            readOnly = true
        )
        WhereNowOutlinedTextField(
            text = airport.value,
            label = stringResource(R.string.trip_details_airport_name_label),
            readOnly = true
        )
    }
}

@Composable
private fun DistanceArea(
    modifier: Modifier = Modifier,
    distance: String
) {
    val distanceKm = remember { mutableStateOf(distance) }

    Column(
        modifier = modifier
    ) {
        WhereNowOutlinedTextField(
            modifier = Modifier
                .padding(vertical = MaterialTheme.whereNowSpacing.space24)
                .align(Alignment.CenterHorizontally),
            text = distanceKm.value,
            label = stringResource(R.string.trip_details_distance_label),
            readOnly = true
        )
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