package com.example.wherenow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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

val DRAWER_SHEET_WIDTH = 300.dp

@Composable
fun WhereNowModalNavigationDrawer(
    drawerState: DrawerState,
    contentPage: @Composable () -> Unit,
    statesVisitedClick: () -> Unit,
    closeAppClick: () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .width(DRAWER_SHEET_WIDTH)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.whereNowSpacing.space16)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(MaterialTheme.whereNowSpacing.space24))
                    Text(
                        modifier = Modifier.semantics { heading() },
                        text = stringResource(R.string.trip_list_navigation_drawer_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space16))
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.trip_list_navigation_drawer_states),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.List,
                                contentDescription = null
                            )
                        },
                        onClick = { statesVisitedClick() }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.trip_list_navigation_drawer_close_app),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = null
                            )
                        },
                        onClick = { closeAppClick() }
                    )
                }
            }
        }
    ) {
        contentPage()
    }
}

@PreviewLightDark
@Composable
private fun WhereNowModalNavigationDrawerPreview() {
    WhereNowTheme {
        WhereNowModalNavigationDrawer(
            closeAppClick = {},
            drawerState = DrawerState(initialValue = DrawerValue.Closed),
            contentPage = {},
            statesVisitedClick = {}
        )
    }
}