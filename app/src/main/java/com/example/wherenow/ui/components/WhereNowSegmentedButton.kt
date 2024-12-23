package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.theme.WhereNowTheme

@Composable
fun WhereNowSegmentedButton(
    modifier: Modifier = Modifier,
    options: List<TripListDataEnum>,
    onSelectedIndexClick: (TripListDataEnum) -> Unit,
    selectedButtonType: TripListDataEnum
) {
    val checkedList = remember { mutableStateListOf<Int>() }
    val icons = listOf(
        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        Icons.Filled.KeyboardArrowDown,
        Icons.AutoMirrored.Filled.KeyboardArrowRight
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    icon = {
                        SegmentedButtonDefaults.Icon(active = index in checkedList) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = null,
                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                            )
                        }
                    },
                    selected = false,
                    onClick = {
                        onSelectedIndexClick(
                            when (index) {
                                0 -> TripListDataEnum.PAST
                                1 -> TripListDataEnum.PRESENT
                                else -> TripListDataEnum.FUTURE
                            }
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeBorderColor = when (selectedButtonType) {
                            TripListDataEnum.PAST -> MaterialTheme.colorScheme.outline
                            TripListDataEnum.PRESENT -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        },
                        activeContainerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = label.toString().lowercase().replaceFirstChar { it.uppercaseChar() },
                        color = when (selectedButtonType) {
                            TripListDataEnum.PAST -> MaterialTheme.colorScheme.outline
                            TripListDataEnum.PRESENT -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowSegmentedButtonPresentPreview() {
    WhereNowTheme {
        WhereNowSegmentedButton(
            options = listOf(TripListDataEnum.PAST, TripListDataEnum.PRESENT, TripListDataEnum.FUTURE),
            onSelectedIndexClick = {},
            selectedButtonType = TripListDataEnum.PRESENT
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowSegmentedButtonPastPreview() {
    WhereNowTheme {
        WhereNowSegmentedButton(
            options = listOf(TripListDataEnum.PAST, TripListDataEnum.PRESENT, TripListDataEnum.FUTURE),
            onSelectedIndexClick = {},
            selectedButtonType = TripListDataEnum.PAST
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowSegmentedButtonFuturePreview() {
    WhereNowTheme {
        WhereNowSegmentedButton(
            options = listOf(TripListDataEnum.PAST, TripListDataEnum.PRESENT, TripListDataEnum.FUTURE),
            onSelectedIndexClick = {},
            selectedButtonType = TripListDataEnum.FUTURE
        )
    }
}