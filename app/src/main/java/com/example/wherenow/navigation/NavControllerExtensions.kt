package com.example.wherenow.navigation

import androidx.navigation.NavController
import com.example.wherenow.util.StringUtils

fun NavController.navigateBack(route: String, inclusive: Boolean = false, saveState: Boolean = false) {
    popBackStack(
        route = route,
        inclusive = inclusive,
        saveState = saveState
    )
}

fun NavController.navigateToTripTileDetails(tripId: Int) {
    navigate(Screen.TripTileDetails.passArgs(tripId))
}

fun NavController.navigateToImportantNotes(tripId: Int) {
    navigate(Screen.ImportantNotes.passArgs(tripId))
}

fun NavController.navigateToTripDetails() {
    navigate(Screen.TripDetails.route)
}

fun NavController.navigateToFileViewer(tripId: Int) {
    navigate(Screen.FileViewer.passArgs(tripId))
}

fun NavController.navigateToError() {
    navigate(Screen.Error.route)
}

fun NavController.navigateToTripList() {
    navigate(Screen.TripList.route)
}

fun NavController.navigateToStatesVisited() {
    navigate(Screen.StatesVisited.route)
}

fun NavController.navigateToBlankNote(
    titleEditNote: String = StringUtils.EMPTY,
    descriptionEditNote: String = StringUtils.EMPTY,
    idNote: String = StringUtils.EMPTY,
    tripId: String = StringUtils.EMPTY
) {
    val route = Screen.BlankNote.passArgs(titleEditNote, descriptionEditNote, idNote, tripId)
    navigate(route)
}