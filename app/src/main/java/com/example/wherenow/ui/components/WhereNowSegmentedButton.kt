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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.util.textWithFirstUppercaseChar

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
        val segmentedButtonDescription = stringResource(R.string.accessibility_segmented_button)

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    modifier = Modifier.semantics {
                        role = Role.Button
                        contentDescription = segmentedButtonDescription
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    icon = {
                        SegmentedButtonDefaults.Icon(active = index in checkedList) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = null,
                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize),
                                tint = when (selectedButtonType) {
                                    TripListDataEnum.PAST -> MaterialTheme.colorScheme.primary
                                    TripListDataEnum.PRESENT -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.onTertiaryContainer
                                }
                            )
                        }
                    },
                    selected = when (selectedButtonType) {
                        TripListDataEnum.PAST -> {
                            index == 0
                        }

                        TripListDataEnum.PRESENT -> {
                            index == 1
                        }

                        else -> {
                            index == 2
                        }
                    },
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
                            TripListDataEnum.PAST -> MaterialTheme.colorScheme.primary
                            TripListDataEnum.PRESENT -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        },
                        activeContainerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = label.toString().textWithFirstUppercaseChar(),
                        color = when (selectedButtonType) {
                            TripListDataEnum.PAST -> MaterialTheme.colorScheme.primary
                            TripListDataEnum.PRESENT -> MaterialTheme.colorScheme.secondary
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