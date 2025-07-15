package com.example.wherenow.ui.app.settingsmenu.statesvisited

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedViewState
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatesVisitedUiIntent
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.Size
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.testutil.TestTag.CHECKBOX_TAG
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_STATES_VISITED_KEY = "NavigationStatesVisitedEvents"

@Composable
internal fun StatedVisitedScreen(
    navigationEvent: (StatedVisitedNavigationEvent) -> Unit
) {
    val context = LocalContext.current

    val viewModel: StatedVisitedViewModel = koinViewModel()
    StatedVisitedContent(
        state = viewModel.uiState.collectAsState().value,
        intent = viewModel::onUiIntent
    )
    LaunchedEffect(NAVIGATION_STATES_VISITED_KEY) {
        viewModel.loadData(context)
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
internal fun StatedVisitedContent(
    state: StatedVisitedViewState,
    intent: (StatesVisitedUiIntent) -> Unit
) {
    if (state.showAnimation) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LaunchedEffect(state.showAnimation) {
                if (state.showAnimation) {
                    delay(3000)
                    intent(StatesVisitedUiIntent.MarkAnimationShown)
                }
            }
            CongratulationsForVisitAllStates()
        }
    } else {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            topBar = {
                WhereNowToolbar(
                    toolbarTitle = stringResource(R.string.states_visited_title),
                    onBackAction = { intent(StatesVisitedUiIntent.OnBackClicked) }
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
                            )
                            .semantics(mergeDescendants = true) {},
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(visitedState.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(Size().size30)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.whereNowSpacing.space16))
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .testTag(if (check) "Strikethrough_${visitedState.id}" else "Normal_${visitedState.id}"),
                            text = visitedState.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            textDecoration = if (check) TextDecoration.LineThrough else null,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Checkbox(
                            checked = check,
                            onCheckedChange = { checked ->
                                intent(
                                    StatesVisitedUiIntent.OnCheckboxToggled(
                                        visitedState.id,
                                        checked
                                    )
                                )
                            },
                            modifier = Modifier
                                .semantics {
                                    role = Role.Checkbox
                                }
                                .testTag("$CHECKBOX_TAG${visitedState.id}")
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.whereNowSpacing.space8))
                }
            }
        }
    }
}

@Composable
private fun CongratulationsForVisitAllStates() {
    val congratsAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.congratulation_for_visiting_all_states)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = MaterialTheme.whereNowSpacing.space16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = congratsAnimation,
            alignment = Alignment.BottomCenter,
            modifier = Modifier
                .size(Size().size350)
                .semantics {
                    contentDescription = StringUtils.EMPTY
                }
        )
        Text(
            text = stringResource(R.string.state_congratulations),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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