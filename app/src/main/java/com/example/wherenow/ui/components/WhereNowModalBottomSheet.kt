package com.example.wherenow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme

const val MODAL_TITLE = "Modal title"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereNowModalBottomSheet() {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    Box {
        Button(
            onClick = { isSheetOpen = true }
        ) {
            Text(text = "modal")
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                TitleArea(
                    onModalClose = { isSheetOpen = false },
                    modifier = Modifier.padding(24.dp)
                )
                CityCodeArea(
                    modifier = Modifier.padding(24.dp),
                    departureCode = "WAW",
                    arrivalCode = "GDA",
                )
                DetailsArea()
                DistanceArea(
                    distance = "250 km"
                )
                Spacer(Modifier.weight(1f))
                WhereNowButton(
                    buttonText = stringResource(R.string.button_text),
                    onClick = {/*TODO*/ }
                )
            }
        }
    }
}

@Composable
private fun TitleArea(
    onModalClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.modal_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = MODAL_TITLE,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onModalClose() }
        )
    }
}

@Composable
private fun DateInput() {
    //TODOm Date picker?
}

@Composable
private fun CityCodeArea(
    departureCode: String,
    arrivalCode: String,
    modifier: Modifier = Modifier
) {
    val modalAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.modal_animation))

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val textDeparture = remember { mutableStateOf(departureCode) }
        val textArrival = remember { mutableStateOf(arrivalCode) }

        WhereNowOutlinedTextField(
            modifier = Modifier.weight(1f),
            text = textDeparture.value,
            placeholder = stringResource(R.string.modal_code_placeholder),
            label = stringResource(R.string.modal_departure)
        )
        LottieAnimation(
            composition = modalAnimation,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .weight(1f)
        )
        WhereNowOutlinedTextField(
            modifier = Modifier.weight(1f),
            text = textArrival.value,
            placeholder = stringResource(R.string.modal_code_placeholder),
            label = stringResource(R.string.modal_arrival)
        )
    }
}

@Composable
private fun DetailsArea() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailsFlightArea(
            modifier = Modifier.weight(1f),
            city = "Gdańsk",
            countryName = "Polska",
            airportName = "GDA"
        )
        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(220.dp)
        )
        DetailsFlightArea(
            modifier = Modifier.weight(1f),
            city = "Gdańsk",
            countryName = "Polska",
            airportName = "GDA"
        )
    }
}

@Composable
private fun DetailsFlightArea(
    modifier: Modifier = Modifier,
    city: String,
    countryName: String,
    airportName: String
) {
    val cityName = remember { mutableStateOf(city) }
    val countryName = remember { mutableStateOf(countryName) }
    val airportName = remember { mutableStateOf(airportName) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        WhereNowOutlinedTextField(
            modifier = Modifier.padding(bottom = 8.dp),
            text = cityName.value,
            label = stringResource(R.string.modal_city_label),
            readOnly = true
        )
        WhereNowOutlinedTextField(
            modifier = Modifier.padding(bottom = 8.dp),
            text = countryName.value,
            label = stringResource(R.string.modal_country_label),
            readOnly = true
        )
        WhereNowOutlinedTextField(
            text = airportName.value,
            label = stringResource(R.string.modal_airport_name_label),
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
                .padding(vertical = 24.dp)
                .align(Alignment.CenterHorizontally),
            text = distanceKm.value,
            label = stringResource(R.string.modal_distance_label),
            readOnly = true
        )
    }
}

@PreviewLightDark
@Composable
private fun WhereNowModalBottomSheetPreview() {
    WhereNowTheme {
        WhereNowModalBottomSheet()
    }
}