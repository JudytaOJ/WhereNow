package com.example.wherenow.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.error.ErrorScreen
import com.example.wherenow.ui.app.settingsmenu.statesvisited.StatedVisitedScreen
import com.example.wherenow.ui.app.settingsmenu.statesvisited.models.StatedVisitedNavigationEvent
import com.example.wherenow.ui.app.splashScreen.SplashScreen
import com.example.wherenow.ui.app.tripdatadetails.TripDataDetailsScreen
import com.example.wherenow.ui.app.tripdatadetails.models.TripDataDetailsNavigationEvent
import com.example.wherenow.ui.app.triplist.TripListScreen
import com.example.wherenow.ui.app.triplist.model.TripListNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.TripTileDetailsScreen
import com.example.wherenow.ui.app.triptiledetails.filetile.FileScreen
import com.example.wherenow.ui.app.triptiledetails.filetile.model.FileNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.ImportantNotesScreen
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.BlankNoteScreen
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.model.BlankNoteNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.importantnotes.model.ImportantNotesNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsNavigationEvent
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag
import com.example.wherenow.util.StringUtils

@Composable
fun NavHost(
    navController: NavHostController = rememberNavController(),
    onCloseApp: () -> Unit,
    openFile: (FileData) -> Unit
) {
    Surface {
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route
        ) {

            // Splash
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }

            // Trip List
            composable(Screen.TripList.route) {
                TripListScreen { event ->
                    when (event) {
                        TripListNavigationEvent.OnAddTrip -> navController.navigateToTripDetails()
                        TripListNavigationEvent.OnCloseApp -> onCloseApp()
                        TripListNavigationEvent.StatesVisited -> navController.navigateToStatesVisited()
                        is TripListNavigationEvent.OnShowDetailsTrip -> navController.navigateToTripTileDetails(tripId = event.tileId)
                    }
                }
            }

            // Trip Details
            composable(Screen.TripDetails.route) {
                TripDataDetailsScreen { event ->
                    when (event) {
                        TripDataDetailsNavigationEvent.OnBackNavigation -> navController.navigateBack(
                            route = Screen.TripList.route
                        )

                        TripDataDetailsNavigationEvent.OnErrorScreen -> navController.navigateToError()
                        TripDataDetailsNavigationEvent.OnNextClicked -> navController.navigateToTripList()
                    }
                }
            }

            // Error
            composable(Screen.Error.route) {
                ErrorScreen {
                    navController.navigateToTripList()
                }
            }

            // Trip Tile
            composable(
                route = Screen.TripTileDetails.route,
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                TripTileDetailsScreen { event ->
                    when (event) {
                        TripTileDetailsNavigationEvent.OnBack -> navController.navigateBack(
                            route = Screen.TripList.route
                        )

                        is TripTileDetailsNavigationEvent.ImportantNotesDetails -> navController.navigateToImportantNotes(tripId = event.tripId)
                        is TripTileDetailsNavigationEvent.AddFiles -> navController.navigateToFileViewer(tripId = event.tripId)
                    }
                }
            }

            // Important Notes
            composable(
                route = Screen.ImportantNotes.route,
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                ImportantNotesScreen { event ->
                    when (event) {
                        ImportantNotesNavigationEvent.OnBack ->
                            navController.navigateBack(
                                route = Screen.TripTileDetails.route,
                                inclusive = false,
                                saveState = false
                            )

                        is ImportantNotesNavigationEvent.OnAddNotes ->
                            navController.navigateToBlankNote(
                                titleEditNote = StringUtils.EMPTY,
                                descriptionEditNote = StringUtils.EMPTY,
                                idNote = StringUtils.EMPTY,
                                tripId = event.tripId.toString()
                            )

                        is ImportantNotesNavigationEvent.OnEditNote ->
                            navController.navigateToBlankNote(
                                titleEditNote = event.note.title,
                                descriptionEditNote = event.note.description,
                                idNote = event.note.id.toString(),
                                tripId = event.note.tripId.toString()
                            )
                    }
                }
            }

            // Blank Note
            composable(
                route = Screen.BlankNote.route,
                arguments = listOf(
                    navArgument(TripTileDetailsTag.TITLE_EDIT_NOTE) { type = NavType.StringType; defaultValue = StringUtils.EMPTY },
                    navArgument(TripTileDetailsTag.DESCRIPTION_EDIT_NOTE) { type = NavType.StringType; defaultValue = StringUtils.EMPTY },
                    navArgument(TripTileDetailsTag.ID_NOTE) { type = NavType.IntType; defaultValue = 0 },
                    navArgument(TripTileDetailsTag.TRIP_ID) { type = NavType.IntType; defaultValue = 0 }
                )
            ) {
                BlankNoteScreen { event ->
                    when (event) {
                        BlankNoteNavigationEvent.OnBackClicked -> navController.navigateBack(
                            route = Screen.ImportantNotes.route,
                            inclusive = false,
                            saveState = false
                        )

                        is BlankNoteNavigationEvent.AddClickedEvent -> navController.navigateToImportantNotes(tripId = event.tripId)
                    }
                }
            }

            // States Visited
            composable(Screen.StatesVisited.route) {
                StatedVisitedScreen { event ->
                    when (event) {
                        StatedVisitedNavigationEvent.OnBackNavigation -> navController.navigateBack(
                            route = Screen.TripList.route
                        )
                    }
                }
            }

            // File Viewer
            composable(
                route = Screen.FileViewer.route,
                arguments = listOf(navArgument("tripId") { type = NavType.IntType })
            ) {
                FileScreen { event ->
                    when (event) {
                        FileNavigationEvent.OnBackClicked -> navController.navigateBack(
                            route = Screen.TripTileDetails.route
                        )

                        is FileNavigationEvent.OnOpenFile -> openFile(event.file)
                    }
                }
            }
        }
    }
}