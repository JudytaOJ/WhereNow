package com.example.wherenow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.Elevation
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.testutil.TestTag.FLIGHT_TILE_TAG

val BORDER = 1.dp

@Composable
fun WhereNowDetailsFlightTile(
    cardDescription: String,
    cardSupportedText: String? = null,
    image: Painter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .semantics { role = Role.Button }
            .testTag(FLIGHT_TILE_TAG),
        border = BorderStroke(BORDER, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation().elevation10),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.whereNowSpacing.space16),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = MaterialTheme.whereNowSpacing.space8)
                        .semantics { heading() },
                    text = cardDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    painter = image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                modifier = Modifier.padding(top = MaterialTheme.whereNowSpacing.space8),
                text = cardSupportedText.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsFlightTilePreview() {
    WhereNowTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            WhereNowDetailsFlightTile(
                cardDescription = "Details flight",
                cardSupportedText = "You will find such flight details as names of cities, airports or the distance separating the place of departure and arrival",
                image = painterResource(R.drawable.flight_icon),
                onClick = {}
            )
        }
    }
}