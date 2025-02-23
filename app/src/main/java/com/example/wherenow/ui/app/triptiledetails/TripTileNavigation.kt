package com.example.wherenow.ui.app.triptiledetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag

internal const val TILE_ID = TripTileDetailsTag.TILE_ID
internal const val TILE_DETAILS_ROUTE = "wherenow/ui/app/tripTile?{$TILE_ID}"

internal fun NavController.navigateToTripTile(tileId: String) {
    navigate(
        TILE_DETAILS_ROUTE.replace("{$TILE_ID}", tileId)
    )
}

internal fun NavGraphBuilder.tripTile(
    onNavigateBack: () -> Unit
) {
    composable(
        route = TILE_DETAILS_ROUTE,
        arguments = listOf(
            navArgument(TILE_ID) {
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ) {
        TripTileDetailsScreen { events ->
            when (events) {
                TripTileDetailsNavigationEvent.OnBack -> onNavigateBack()
            }
        }
    }
}