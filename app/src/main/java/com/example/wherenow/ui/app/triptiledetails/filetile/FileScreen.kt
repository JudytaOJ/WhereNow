package com.example.wherenow.ui.app.triptiledetails.filetile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.wherenow.R
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileUiIntent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileViewState
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowImportantMessage
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_FILE_KEY = "NavigationFileKey"
val DELETE_SIZE = 24.dp
const val PDF = "application/pdf"
val GRID_CELLS = 150.dp
val CARD_ELEVATION = 4.dp
val IMAGE_SIZE = 100.dp
val SIZE_EMPTY_STATE = 350.dp
const val REMOVE_FILE_DESCRIPTION = "Remove file"

@Composable
internal fun FileScreen(
    navigationEvent: (FileNavigationEvent) -> Unit
) {
    val viewModel: FileViewModel = koinViewModel()
    FileContentScreen(
        uiIntent = viewModel::onUiIntent,
        state = viewModel.uiState.collectAsState().value,
        getFileNameFromUri = viewModel::getFileNameFromUri
    )
    LaunchedEffect(NAVIGATION_FILE_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
private fun FileContentScreen(
    uiIntent: (FileUiIntent) -> Unit,
    state: FileViewState,
    getFileNameFromUri: (Uri) -> String
) {
    BackHandler(true) { uiIntent(FileUiIntent.OnBackClicked) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val choosePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val name = getFileNameFromUri(it)

                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                scope.launch {
                    uiIntent(
                        FileUiIntent.AddFile(
                            FileData(
                                name = name,
                                url = it.toString(),
                                id = 0,
                                tripId = state.tripId
                            )
                        )
                    )
                }
            }
        }
    )

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            WhereNowToolbar(
                toolbarTitle = stringResource(R.string.file_title),
                onBackAction = { uiIntent(FileUiIntent.OnBackClicked) }
            )
        },
        floatingActionButton = {
            WhereNowFloatingActionButton(
                onClick = { choosePdfLauncher.launch(arrayOf(PDF)) }
            )
        },
        content = { paddingValue ->
            if (state.fileList.isEmpty()) {
                EmptyStateFile(
                    modifier = Modifier.padding(paddingValue)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                        .padding(top = MaterialTheme.whereNowSpacing.space16)
                ) {
                    WhereNowImportantMessage(
                        message = stringResource(R.string.file_alert)
                    )
                    LazyVerticalGrid(
                        modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space16),
                        columns = GridCells.Adaptive(minSize = GRID_CELLS),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16)
                    ) {
                        items(
                            items = state.fileList,
                            key = { id -> id.id }
                        ) {
                            FileItem(
                                onClicked = {
                                    uiIntent(
                                        FileUiIntent.OpenFile(
                                            id = FileData(
                                                name = it.name,
                                                url = it.url,
                                                id = it.id,
                                                tripId = it.tripId
                                            )
                                        )
                                    )
                                },
                                onDeleteClicked = { uiIntent(FileUiIntent.OnDeleteFile(id = it.id)) },
                                name = it.name
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun FileItem(
    name: String,
    onClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.whereNowSpacing.space4)
            .clickable(onClick = { onClicked() }),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(CARD_ELEVATION)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.whereNowSpacing.space8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.file_image),
                contentDescription = null,
                modifier = Modifier.size(IMAGE_SIZE)
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space8)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    modifier = Modifier
                        .padding(start = MaterialTheme.whereNowSpacing.space4)
                        .size(DELETE_SIZE)
                        .clickable { onDeleteClicked() }
                        .background(Color.Transparent),
                    imageVector = Icons.Rounded.Delete,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = REMOVE_FILE_DESCRIPTION
                )
            }
        }
    }
}

@Composable
private fun EmptyStateFile(
    modifier: Modifier = Modifier
) {
    val fileEmptyStateAnimation by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.file_empty_state)
    )
    val emptyAnimationProgress by animateLottieCompositionAsState(
        composition = fileEmptyStateAnimation,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 0.5f
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            LottieAnimation(
                composition = fileEmptyStateAnimation,
                progress = emptyAnimationProgress,
                modifier = Modifier
                    .size(SIZE_EMPTY_STATE)
                    .align(Alignment.Center)
                    .semantics {
                        contentDescription = StringUtils.EMPTY
                    }
            )

            Text(
                text = stringResource(R.string.file_empty_list),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = MaterialTheme.whereNowSpacing.space40)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun FileContentScreenEmptyStatePreview() {
    WhereNowTheme {
        FileContentScreen(
            uiIntent = {},
            state = FileViewState(),
            getFileNameFromUri = { _ -> "Preview.pdf" }
        )
    }
}

@PreviewLightDark
@Composable
private fun FileContentScreenPreview() {
    WhereNowTheme {
        FileContentScreen(
            uiIntent = {},
            state = FileViewState(
                fileList = listOf(
                    FileData(
                        name = "Ticket from New York",
                        url = "content://com.example.provider/files/sample.pdf",
                        id = 2,
                        tripId = 1
                    ),
                    FileData(
                        name = "Ticket to Detroit",
                        url = "content://com.example.provider/files/sample.pdf",
                        id = 3,
                        tripId = 1
                    ),
                    FileData(
                        name = "New york parking",
                        url = "content://com.example.provider/files/sample.pdf",
                        id = 4,
                        tripId = 1
                    )
                )
            ),
            getFileNameFromUri = { _ -> "Preview.pdf" }
        )
    }
}