package com.example.wherenow.ui.app.settingsmenu.flightStatistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.components.USAMap
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsUiIntent
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.models.FlightStatisticsViewState
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StatisticsString
import com.example.wherenow.util.StringUtils
import kotlinx.serialization.json.JsonElement
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_FLIGHT_STATISTICS_KEY = "NavigationFlightStatisticsEvents"

@Composable
internal fun FlightStatisticsScreen(
    navigationEvent: (FlightStatisticsNavigationEvent) -> Unit
) {
    val viewModel: FlightStatisticsViewModel = koinViewModel()
    FlightStatisticsContent(
        intent = viewModel::onUiIntent,
        state = viewModel.uiState.collectAsState().value
    )
    LaunchedEffect(NAVIGATION_FLIGHT_STATISTICS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
internal fun FlightStatisticsContent(
    state: FlightStatisticsViewState,
    intent: (FlightStatisticsUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.states_visited_title),
                onBackAction = { intent(FlightStatisticsUiIntent.OnBackClicked) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
                .padding(MaterialTheme.whereNowSpacing.space24)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatisticsBox(state = state)
            YourActivity(
                features = state.features.orEmpty(),
                visitedStates = state.statedVisited
            )
            YourFlights(state = state)
        }
    }
}

@Composable
private fun StatisticsBox(
    state: FlightStatisticsViewState
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.whereNowSpacing.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    StatisticsString(
                        title = state.totalDistance.toString(),
                        subtitle = stringResource(R.string.statistics_total_distance)
                    )
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            VerticalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.whereNowSpacing.space4))
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    StatisticsString(
                        title = state.totalFlight.toString(),
                        subtitle = stringResource(R.string.statistics_total_flight_subtitle)
                    )
                },
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
            VerticalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.whereNowSpacing.space4))
            Text(
                modifier = Modifier.weight(1f),
                text = buildAnnotatedString {
                    StatisticsString(
                        title = buildString {
                            append(state.statedVisited.size.toString())
                            append(StringUtils.SLASH)
                            append(stringResource(R.string.statistics_states_USA_number))
                        },
                        subtitle = stringResource(R.string.statistics_states)
                    )
                },
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun YourFlights(
    state: FlightStatisticsViewState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.whereNowSpacing.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.statistics_your_flight_header),
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider(modifier = Modifier.padding(start = MaterialTheme.whereNowSpacing.space8))
        }
        YourFlightBox(
            icon = painterResource(R.drawable.flight_icon),
            title = state.mostFrequentRoute,
            subtitle = stringResource(R.string.statistics_most_frequent_route)
        )
        YourFlightBox(
            icon = painterResource(R.drawable.flight_icon),
            title = buildString {
                append(state.longestFlight)
                append(StringUtils.SLASH)
                append(state.shortestFlight)
                append(StringUtils.SPACE)
                append(stringResource(R.string.trip_details_miles))
            },
            subtitle = stringResource(R.string.statistics_longest_shortest_flight)
        )
        YourFlightBox(
            icon = painterResource(R.drawable.flight_icon),
            title = "Alabama", //TODO
            subtitle = "State #1" //TODO
        )
        YourFlightBox(
            icon = painterResource(R.drawable.flight_icon),
            title = "3x", //TODO
            subtitle = "the longest flight" //TODO
        )
    }
}

@Composable
private fun YourFlightBox(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    subtitle: String?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(MaterialTheme.whereNowSpacing.space16)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = MaterialTheme.whereNowSpacing.space8),
            painter = icon,
            contentDescription = null
        )
        Column {
            Text(
                text = buildAnnotatedString {
                    StatisticsString(
                        title = title,
                        subtitle = subtitle.orEmpty()
                    )
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun YourActivity(
    features: List<JsonElement>,
    visitedStates: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.whereNowSpacing.space24)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.whereNowSpacing.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.statistics_visited_states_header),
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider(modifier = Modifier.padding(start = MaterialTheme.whereNowSpacing.space8))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            USAMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                visitedStates = visitedStates,
                features = features
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun FlightStatisticsContentPreview() {
    WhereNowTheme {
        FlightStatisticsContent(
            intent = {},
            state = FlightStatisticsViewState(
                statedVisited = listOf("Georgia", "Hawaii", "Alabama"),
                totalFlight = 3,
                totalDistance = 5213,
                mostFrequentRoute = "Los Angeles-New York",
                longestFlight = 1234,
                shortestFlight = 123
            )
        )
    }
}