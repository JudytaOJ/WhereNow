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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.ui.app.triplist.model.TripListDataEnum
import com.example.wherenow.ui.theme.WhereNowTheme

@Composable
fun WhereNowSegmentedButton(
    modifier: Modifier = Modifier,
    options: List<TripListDataEnum>
) {
    val checkedList = remember { mutableStateListOf<Int>() }
    val icons = listOf(
        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        Icons.Filled.KeyboardArrowDown,
        Icons.AutoMirrored.Filled.KeyboardArrowRight
    )
    var selectedIndex by remember { mutableIntStateOf(1) }

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
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    colors = SegmentedButtonDefaults.colors(
                        activeBorderColor = when (selectedIndex) {
                            0 -> MaterialTheme.colorScheme.outline
                            1 -> MaterialTheme.colorScheme.onPrimaryContainer
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        },
                        activeContainerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = label.toString(),
                        color = when (selectedIndex) {
                            0 -> MaterialTheme.colorScheme.outline
                            1 -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onTertiaryContainer
                        }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun WhereNowSegmentedButtonPreview() {
    WhereNowTheme {
        WhereNowSegmentedButton(
            options = listOf(TripListDataEnum.PAST, TripListDataEnum.PRESENT, TripListDataEnum.FUTURE)
        )
    }
}