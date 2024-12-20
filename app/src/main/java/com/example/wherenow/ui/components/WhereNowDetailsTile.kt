package com.example.wherenow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import java.time.LocalDate

val HEIGHT_CARD = 150.dp
val BORDER_STROKE = 1.dp
val DEFAULT_ELEVATION = 10.dp
val DELETE_ICON_SIZE = 20.dp

@Composable
fun WhereNowDetailsTile(
    city: String,
    country: String,
    date: String,
    timeTravel: Int,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(HEIGHT_CARD),
        border = BorderStroke(BORDER_STROKE, MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = DEFAULT_ELEVATION
        ),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = when {
                timeTravel == LocalDate.now().year -> MaterialTheme.colorScheme.primary
                timeTravel >= LocalDate.now().year -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.outline
            },
            disabledContentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space16)
        ) {
            Row(
                modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space16)
            ) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = buildString {
                        append(city)
                        append(StringUtils.COMMA.plus(StringUtils.SPACE))
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = country,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(DELETE_ICON_SIZE)
                        .clickable { onDeleteClick() },
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Remove tile",
                    tint = when {
                        timeTravel < LocalDate.now().year -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onPrimary
                    },
                )
            }
            Text(
                modifier = Modifier.padding(bottom = MaterialTheme.whereNowSpacing.space2),
                text = stringResource(R.string.card_departure_date),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space4),
                color = if (timeTravel >= LocalDate.now().year) MaterialTheme.colorScheme.surfaceTint else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = when {
                    timeTravel == LocalDate.now().year -> stringResource(R.string.card_travel_now)
                    timeTravel > LocalDate.now().year -> stringResource(R.string.card_travel_in_future)
                    else -> stringResource(R.string.card_travel_complete)
                },
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileTravelNowPreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "Lizbona",
            country = "Portugalia",
            date = "20 listopad 2024",
            timeTravel = 2024,
            onClick = {},
            onDeleteClick = {}
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileWaitForTravelPreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "Lizbona",
            country = "Portugalia",
            date = "20 listopad 2026",
            timeTravel = 2026,
            onClick = {},
            onDeleteClick = {}
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileTravelCompletePreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "Lizbona",
            country = "Portugalia",
            date = "20 listopad 2026",
            timeTravel = 2020,
            onClick = {},
            onDeleteClick = {}
        )
    }
}