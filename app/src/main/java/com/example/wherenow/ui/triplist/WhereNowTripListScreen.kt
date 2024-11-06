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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.navigation.Screen
import com.example.wherenow.ui.components.WhereNowDetailsTile
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.triplist.model.WhereNowTripListNavigationEvent
import com.example.wherenow.ui.triplist.model.WhereNowTripListUiIntent
import java.time.LocalDate

val SIZE_EMPTY_STATE_ANIMATION = 300.dp

data class Test(
    val test: String
)

@Composable
internal fun WhereNowListTrip(
    navController: NavController,
    navigationEvent: (WhereNowTripListNavigationEvent) -> Unit
) {
    val viewModel: WhereNowTripListViewModel = viewModel()
    WhereNowTripList(
        navController = navController,
        state = viewModel.uiState.collectAsState().value,
        uiIntent = viewModel::onUiIntent
    )
}

@Composable
private fun WhereNowTripList(
    navController: NavController,
    state: WhereNowTripListViewState,
    uiIntent: (WhereNowTripListUiIntent) -> Unit
) {
    BackHandler {
        navController.navigate(Screen.HOME) {
            popUpTo(Screen.HOME) {
                inclusive = true
            }
        }
    }
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.app_name),
                onChangeAppMode = { uiIntent(WhereNowTripListUiIntent.OnChangeMode) },
                isArrowVisible = false
            )
        }
    ) { padding ->
        if (state.tripList.isEmpty()) {
            WhereNowEmptyStateList()
        } else {
            Column(modifier = Modifier.padding(padding)) {
                Spacer(modifier = Modifier.padding(24.dp))
                WhereNowTripListContent(
                    state = state
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WhereNowDetailsTile(
            city = state.cityName,
            country = state.countryName,
            date = state.date,
            timeTravel = state.timeTravel,
            countDays = state.countDays,
            onClick = {}
        )
        WhereNowDetailsTile(
            city = state.cityName,
            country = state.countryName,
            date = state.date,
            timeTravel = state.timeTravel,
            countDays = state.countDays,
            onClick = {}
        )
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
            .padding(16.dp),
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
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(R.string.trip_list_empty_state),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewLightDark
@Composable
private fun WhereNowTripListPreview() {
    val navController = rememberNavController()
    val state = WhereNowTripListViewState(
        cityName = "Nowy Jork",
        countryName = "USA",
        date = "10 listopad 2023",
        timeTravel = LocalDate.now().minusDays(4),
        countDays = 0
    )
    WhereNowTheme {
        WhereNowTripList(
            navController = navController,
            state = state,
            uiIntent = {}
        )
    }
}