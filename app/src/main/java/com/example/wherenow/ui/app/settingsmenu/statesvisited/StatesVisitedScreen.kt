package com.example.wherenow.ui.app.settingsmenu.statesvisited

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesVisitedUiIntent
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_STATES_VISITED_KEY = "NavigationStatesVisitedEvents"
val IMAGE_SIZE = 32.dp

@Composable
internal fun StatedVisitedScreen(
    navigationEvent: (StatedVisitedNavigationEvent) -> Unit
) {
    val viewModel: StatedVisitedViewModel = koinViewModel()
    StatedVisitedContent(
        state = viewModel.uiState.collectAsState().value,
        intent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_STATES_VISITED_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun StatedVisitedContent(
    state: StatedVisitedViewState,
    intent: (StatesVisitedUiIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.states_visited_title),
                onBackAction = { intent(StatesVisitedUiIntent.OnBackClicked) },
                isArrowVisible = true,
                isMenuAppIconVisible = false
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = MaterialTheme.whereNowSpacing.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space8)
        ) {
            items(
                items = state.statesList,
                key = { it.id }
            ) { visitedState ->
                val check = state.statesList.first { it.id == visitedState.id }.isChecked
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.whereNowSpacing.space16,
                            vertical = MaterialTheme.whereNowSpacing.space8
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(visitedState.imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(IMAGE_SIZE)
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space16))
                    Text(
                        text = stringResource(visitedState.text),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        textDecoration = if (check) TextDecoration.LineThrough else null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = check,
                        onCheckedChange = { checked ->
                            intent(StatesVisitedUiIntent.OnCheckboxToggled(visitedState.id, checked))
                        }
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.whereNowSpacing.space8))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun StatedVisitedScreenPreview() {
    val state = StatedVisitedViewState()
    WhereNowTheme {
        StatedVisitedContent(
            state = state,
            intent = {}
        )
    }
}