package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.wherenow.ui.theme.Elevation
import com.example.wherenow.ui.theme.Size
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.testutil.TestTag.NOTES_TILE_DELETE_TAG
import com.example.wherenow.util.testutil.TestTag.NOTES_TILE_TAG

@Composable
fun WhereNowNotesTile(
    titleNotes: String,
    descriptionNotes: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    val fontScale = LocalDensity.current.fontScale

    Card(
        modifier = Modifier
            .size(width = Size().size200, height = Size().size200)
            .testTag(NOTES_TILE_TAG),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation().elevation10),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.secondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = MaterialTheme.whereNowSpacing.space8)
                .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.whereNowSpacing.space8)
                    .semantics { heading() },
                text = titleNotes,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.scrim
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.scrim)
            Text(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.whereNowSpacing.space8)
                    .height(Size().size100),
                text = descriptionNotes,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.scrim
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .scale(fontScale)
                        .clickable { onDeleteClick() }
                        .background(Color.Transparent)
                        .semantics { role = Role.Button }
                        .testTag(NOTES_TILE_DELETE_TAG),
                    imageVector = Icons.Rounded.Delete,
                    tint = MaterialTheme.colorScheme.scrim,
                    contentDescription = "Remove note"
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowNotesTilePreview() {
    WhereNowTheme {
        WhereNowNotesTile(
            titleNotes = "First Note for preview",
            descriptionNotes = LoremIpsum(words = 50).values.joinToString(),
            onClick = {}
        )
    }
}