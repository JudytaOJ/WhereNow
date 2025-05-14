package com.example.wherenow.ui.app.triptiledetails.filetile

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
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

const val NAVIGATION_TILE_DETAILS_KEY = "NavigationTileDetailsKey"
val DELETE_SIZE = 30.dp
const val PDF = "application/pdf"

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
    LaunchedEffect(NAVIGATION_TILE_DETAILS_KEY) {
        viewModel.navigationEvents.collect(navigationEvent)
    }
}

@Composable
internal fun FileContentScreen(
    modifier: Modifier = Modifier,
    uiIntent: (FileUiIntent) -> Unit,
    state: FileViewState,
    getFileNameFromUri: (Context, Uri) -> String
) {
    BackHandler(true) {
        uiIntent(FileUiIntent.OnBackClicked)
    }

    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val scope = rememberCoroutineScope()
    val choosePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val name = getFileNameFromUri(context, it)

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
                onBackAction = { uiIntent(FileUiIntent.OnBackClicked) },
                isArrowVisible = true,
                isMenuAppIconVisible = false
            )
        },
        floatingActionButton = {
            WhereNowFloatingActionButton(
                onClick = {
                    choosePdfLauncher.launch(arrayOf(PDF))
                }
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
                        text = stringResource(R.string.file_empty_list)
                    )
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue),
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.whereNowSpacing.space16)
                ) {
                    items(
                        items = state.fileList,
                        key = { id -> id.id }
                    ) {
                        PhotoItem(
                            photo = pdfUri,
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
                            onDeleteClicked = { uiIntent(FileUiIntent.OnDeleteFile(id = it.id)) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun PhotoItem(
    photo: Uri?,
    onClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = { onClicked() }),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .build(),
                contentDescription = "test",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "coś tam jest",
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
                    contentDescription = "Remove file"
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
            getFileNameFromUri = { _, _ -> "Preview.pdf" }
        )
    }
}