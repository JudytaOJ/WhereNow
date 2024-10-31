package com.example.wherenow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.StringUtils
import java.time.LocalDate

val WIDTH_CARD = 200.dp
val HEIGHT_CARD = 100.dp
val BORDER_STROKE = 1.dp
val DEFAULT_ELEVATION = 10.dp

@Composable
fun WhereNowDetailsTile(
    city: String,
    country: String,
    date: String,
    timeTravel: LocalDate,
    countDays: Int = 0,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.size(width = WIDTH_CARD, height = HEIGHT_CARD),
        border = BorderStroke(BORDER_STROKE, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = DEFAULT_ELEVATION
        ),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = buildString {
                        append(city)
                        append(StringUtils.COMMA.plus(StringUtils.SPACE))
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = country,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = stringResource(R.string.card_departure_date),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = if (timeTravel >= LocalDate.now()) stringResource(
                    R.string.card_time_travel,
                    countDays
                )
                else stringResource(R.string.card_travel_complete),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileTravelCompletePreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "Lizbona",
            country = "Portugalia",
            date = "20 listopad 2024",
            timeTravel = LocalDate.now().minusDays(2),
            onClick = {}
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
            date = "20 listopad 2024",
            timeTravel = LocalDate.now(),
            countDays = 2,
            onClick = {}
        )
    }
}