package com.example.wherenow.ui.components.detailstile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType.US_AUTUMN
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType.US_CHINATOWN
import com.example.wherenow.ui.components.detailstile.WhereNowDetailsTileImageType.US_FLAG
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.StringUtils
import com.example.wherenow.util.convertLocalDateToString
import com.example.wherenow.util.convertLocalDateToTimestampUTC
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
    timeTravel: Long,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    image: Int
) {
    val time = convertLocalDateToTimestampUTC(LocalDate.now())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(HEIGHT_CARD),
        border = BorderStroke(BORDER_STROKE, MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = DEFAULT_ELEVATION
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick.invoke() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .blur(2.dp)
                    .graphicsLayer {
                        this.alpha = 0.6f
                        compositingStrategy = CompositingStrategy.ModulateAlpha
                    },
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
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
                        contentDescription = "Remove tile"
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (date == LocalDate.now().convertLocalDateToString()) {
                        TodayFlightTag()
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space4),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = when {
                        timeTravel == time -> stringResource(R.string.card_travel_complete_today)
                        timeTravel > time &&
                                timeTravel < convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(3)) -> stringResource(R.string.card_travel_now)

                        timeTravel > convertLocalDateToTimestampUTC(LocalDate.now().plusMonths(3)) -> stringResource(R.string.card_travel_in_future)
                        else -> stringResource(R.string.card_travel_complete)
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun TodayFlightTag() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(MaterialTheme.whereNowSpacing.space4),
        ) {
            Text(
                text = stringResource(R.string.trip_list_today_flight_tag),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.inversePrimary
            )
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileTravelNowPreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "New York",
            country = "United States",
            date = "20 november 2024",
            timeTravel = 2024,
            onClick = {},
            onDeleteClick = {},
            image = US_AUTUMN.icon
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileWaitForTravelPreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "New York",
            country = "United States",
            date = "20 november 2026",
            timeTravel = 2026,
            onClick = {},
            onDeleteClick = {},
            image = US_CHINATOWN.icon
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowDetailsTileTravelCompletePreview() {
    WhereNowTheme {
        WhereNowDetailsTile(
            city = "New York",
            country = "United States",
            date = "20 november 2026",
            timeTravel = 2020,
            onClick = {},
            onDeleteClick = {},
            image = US_FLAG.icon
        )
    }
}

@PreviewLightDark
@Composable
fun TodayFlightTagPreview() {
    WhereNowTheme {
        TodayFlightTag()
    }
}