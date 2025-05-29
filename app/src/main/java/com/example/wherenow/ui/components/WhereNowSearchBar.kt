@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.wherenow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.components.textfield.customTextFieldColors
import com.example.wherenow.ui.theme.WhereNowTheme

@Composable
fun WhereNowSearchBar(
    modifier: Modifier = Modifier,
    searchInfo: String,
    onClick: (String) -> Unit,
    onClearText: () -> Unit
) {
    Column {
        SearchBar(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .semantics(mergeDescendants = true) {},
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchInfo,
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = {
                        Text(
                            modifier = Modifier.semantics { heading() },
                            text = stringResource(R.string.trip_details_search_bar_hint),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.clickable { onClearText() },
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.accessibility_search_bar_clear_text),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onQueryChange = { onClick(it) },
                    colors = customTextFieldColors(isSearchBar = true)
                )
            },
            expanded = false,
            onExpandedChange = {},
            colors = SearchBarColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant,
                dividerColor = Color.Transparent
            )
        ) {}
    }
}

@PreviewLightDark
@Composable
private fun WhereNowSearchBarPreview() {
    WhereNowTheme {
        WhereNowSearchBar(
            searchInfo = "New",
            onClick = {},
            onClearText = {}
        )
    }
}