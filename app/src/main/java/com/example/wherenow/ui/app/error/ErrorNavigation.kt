package com.example.wherenow.ui.app.error

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val ERROR_ROUTE = "wherenow/ui/app/error"

internal fun NavController.navigateToError() {
    navigate(ERROR_ROUTE)
}

internal fun NavGraphBuilder.errorScreen(
    onClick: () -> Unit
) {
    composable(
        route = ERROR_ROUTE
    ) {
        ErrorScreen(
            onClick = { onClick() }
        )
    }
}