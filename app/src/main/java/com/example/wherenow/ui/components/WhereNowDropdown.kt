package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.data.dto.AttributesDto
import com.example.wherenow.data.dto.DataItemDto
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereNowDropdown(
    cityName: String,
    cityList: List<AttributesDto>,
    label: String,
    modifier: Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var value by rememberSaveable { mutableStateOf(cityName) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            placeholder = {
                Text(
                    text = "Wybierz",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Localized description"
                    )
                }
            },
            readOnly = true
        )
        DropdownMenu(
            modifier = Modifier
                .padding(MaterialTheme.whereNowSpacing.space16),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cityList.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = city.attributes.city,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        value = city.attributes.city
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        leadingIconColor = MaterialTheme.colorScheme.background,
                        trailingIconColor = MaterialTheme.colorScheme.errorContainer,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DropdownPreview() {
    WhereNowTheme {
        WhereNowDropdown(
            cityName = "Gdańsk",
            cityList = listOf(
                AttributesDto(
                    attributes = DataItemDto(
                        city = "Keflavik",
                        country = "Iceland",
                        iata = "KEF",
                        icao = "KEF",
                        name = "Keflavik Airport"
                    ),
                    id = "1",
                    type = "airport"
                )
            ),
            label = "Wylot",
            modifier = Modifier
        )
    }
}