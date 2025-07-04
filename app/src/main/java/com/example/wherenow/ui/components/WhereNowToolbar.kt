package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.wherenow.R
import com.example.wherenow.ui.theme.Elevation
import com.example.wherenow.ui.theme.Size
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.clickableSingle
import com.example.wherenow.util.testutil.TestTag.BACK_ICON_TAG
import com.example.wherenow.util.testutil.TestTag.MENU_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereNowToolbar(
    toolbarTitle: String,
    onBackAction: () -> Unit = {},
    onMenuAppOpen: () -> Unit = {},
    isArrowVisible: Boolean = true,
    isMenuAppIconVisible: Boolean = false
) {
    TopAppBar(
        modifier = Modifier
            .shadow(elevation = Elevation().elevation10)
            .background(MaterialTheme.colorScheme.surface),
        title = {
            Text(
                modifier = Modifier.semantics {
                    heading()
                    traversalIndex = -1f
                },
                text = toolbarTitle,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            if (isArrowVisible) {
                Row(
                    modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8)
                ) {
                    Icon(
                        modifier = Modifier
                            .clickableSingle(onClick = onBackAction)
                            .semantics {
                                role = Role.Button
                                traversalIndex = 0f
                            }
                            .testTag(BACK_ICON_TAG),
                        contentDescription = stringResource(R.string.accessibility_toolbar_back),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack
                    )
                }
            } else {
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
            }
        },
        actions = {
            if (isMenuAppIconVisible) {
                IconButton(
                    modifier = Modifier
                        .padding(end = MaterialTheme.whereNowSpacing.space4)
                        .testTag(MENU_TAG),
                    onClick = { onMenuAppOpen() }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Size().size24)
                            .semantics { traversalIndex = 1f },
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.accessibility_toolbar_menu),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
fun WhereNowToolbarPreview() {
    WhereNowTheme {
        WhereNowToolbar(
            toolbarTitle = "Where Now",
            onBackAction = {},
            onMenuAppOpen = {}
        )
    }
}

@PreviewLightDark
@Composable
fun WhereNowToolbarWithoutArrowPreview() {
    WhereNowTheme {
        WhereNowToolbar(
            toolbarTitle = "Where Now",
            onBackAction = {},
            onMenuAppOpen = {},
            isArrowVisible = false,
            isMenuAppIconVisible = true
        )
    }
}