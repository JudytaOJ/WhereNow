package com.example.wherenow.ui.app.triplist

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.wherenow.R
import com.example.wherenow.ui.app.settingsmenu.appTheme.AppThemeViewModel
import com.example.wherenow.ui.theme.WhereNowTheme
import com.example.wherenow.ui.theme.whereNowSpacing
import org.koin.androidx.compose.koinViewModel

val DRAWER_SHEET_WIDTH = 300.dp

@Composable
fun ModalNavigationDrawerScreen(
    drawerState: DrawerState,
    contentPage: @Composable () -> Unit,
    statesVisitedClick: () -> Unit,
    closeAppClick: () -> Unit
) {
    val themeViewModel: AppThemeViewModel = koinViewModel()
    val isDarkMode by themeViewModel.isDarkTheme.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = MaterialTheme.whereNowSpacing.space16))
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.trip_list_navigation_drawer_states),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        onClick = { statesVisitedClick() }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(
                                    if (isDarkMode) R.string.trip_list_navigation_drawer_light_theme
                                    else R.string.trip_list_navigation_drawer_dark_theme
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.theme_mode),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        onClick = { themeViewModel.toggleTheme() }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.trip_list_navigation_drawer_close_app),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiary
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
private fun ModalNavigationDrawerPreview() {
    WhereNowTheme {
        ModalNavigationDrawerScreen(
            closeAppClick = {},
            drawerState = DrawerState(initialValue = DrawerValue.Closed),
            contentPage = {},
            statesVisitedClick = {}
        )
    }
}