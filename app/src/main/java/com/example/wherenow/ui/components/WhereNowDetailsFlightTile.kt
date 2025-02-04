package com.example.wherenow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

val WIDTH = 200.dp
val HEIGHT = 100.dp
val BORDER = 1.dp
val ELEVATION = 10.dp
const val DESCRIPTION = "Tile logo"

@Composable
fun WhereNowDetailsFlightTile(
    cardDescription: String,
    image: Painter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(width = WIDTH, height = HEIGHT),
        border = BorderStroke(BORDER, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = ELEVATION),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.whereNowSpacing.space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cardDescription,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = image,
                contentDescription = DESCRIPTION,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsFlightTilePreview() {
    WhereNowTheme {
        WhereNowDetailsFlightTile(
            cardDescription = "Details flight",
            image = painterResource(R.drawable.flight_icon),
            onClick = {}
        )
    }
}