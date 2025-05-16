package com.example.wherenow.ui.app.triptiledetails.filetile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.wherenow.R
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileUiIntent
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileViewState
import com.example.wherenow.ui.components.WhereNowFloatingActionButton
import com.example.wherenow.ui.components.WhereNowToolbar
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val NAVIGATION_FILE_KEY = "NavigationFileKey"
val DELETE_SIZE = 30.dp
const val PDF = "application/pdf"
val GRID_CELLS = 80.dp
val CARD_ELEVATION = 4.dp
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
internal fun FileContentScreen(
    modifier: Modifier = Modifier,
    uiIntent: (FileUiIntent) -> Unit,
    state: FileViewState,
    getFileNameFromUri: (Uri) -> String
) {
    BackHandler(true) { uiIntent(FileUiIntent.OnBackClicked) }

    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
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
                Box(
                    modifier = modifier
                        .padding(paddingValue)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.file_empty_list),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue),
                    columns = GridCells.Adaptive(minSize = GRID_CELLS),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16)
                ) {
                    items(
                        items = state.fileList,
                        key = { id -> id.id }
                    ) {
                        FileItem(
                            uriFile = pdfUri,
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
    )
}

@Composable
fun FileItem(
    uriFile: Uri?,
    name: String,
    onClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    val localContext = LocalContext.current
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
                .background(Color.LightGray)
                .padding(MaterialTheme.whereNowSpacing.space8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(localContext)
                    .data(uriFile)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.background
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    modifier = Modifier
                        .size(DELETE_SIZE)
                        .clickable { onDeleteClicked() }
                        .background(Color.Transparent),
                    imageVector = Icons.Rounded.Delete,
                    tint = MaterialTheme.colorScheme.scrim,
                    contentDescription = REMOVE_FILE_DESCRIPTION
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun FileContentScreenPreview() {
    WhereNowTheme {
        FileContentScreen(
            uiIntent = {},
            state = FileViewState(),
            getFileNameFromUri = { _ -> "Preview.pdf" }
        )
    }
}