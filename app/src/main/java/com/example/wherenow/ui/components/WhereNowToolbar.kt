package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import com.example.wherenow.util.clickableSingle

val SHADOW_TOOLBAR = 10.dp
const val TOOLBAR_DESCRIPTION = "Back action"
const val CLOSE_APP = "Close app"
val CLOSE_APP_ICON_SIZE = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhereNowToolbar(
    toolbarTitle: String,
    onBackAction: () -> Unit = {},
    onCloseApp: () -> Unit = {},
    isArrowVisible: Boolean,
    isCloseAppIconVisible: Boolean
) {
    TopAppBar(
        modifier = Modifier
            .shadow(elevation = SHADOW_TOOLBAR)
            .background(MaterialTheme.colorScheme.surface),
        title = {
            Text(
                modifier = Modifier.semantics {
                    heading()
                    traversalIndex = -1f
                },
                text = toolbarTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                            },
                        contentDescription = TOOLBAR_DESCRIPTION,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                Spacer(modifier = Modifier.padding(MaterialTheme.whereNowSpacing.space8))
            }
        },
        actions = {
            if (isCloseAppIconVisible) {
                IconButton(
                    modifier = Modifier.padding(end = MaterialTheme.whereNowSpacing.space4),
                    onClick = onCloseApp
                ) {
                    Icon(
                        modifier = Modifier
                            .size(CLOSE_APP_ICON_SIZE)
                            .semantics {
                                role = Role.Button
                                traversalIndex = 1f
                            },
                        painter = painterResource(R.drawable.close_app_icon),
                        contentDescription = CLOSE_APP,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
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
            onCloseApp = {},
            isArrowVisible = true,
            isCloseAppIconVisible = false
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
            onCloseApp = {},
            isArrowVisible = false,
            isCloseAppIconVisible = true
        )
    }
}