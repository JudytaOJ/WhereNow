package com.example.wherenow.navigation

import android.net.Uri
import com.example.wherenow.ui.app.triptiledetails.model.TripTileDetailsTag

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object TripList : Screen("trip_list")

    object TripDetails : Screen("trip_details")

    object Error : Screen("error")

    object StatesVisited : Screen("states_visited")

    object FileViewer : Screen("file_viewer/{tripId}") {
        fun passArgs(tripId: Int): String = "file_viewer/$tripId"
    }

    object TripTileDetails : Screen("trip_tile_details/{tripId}") {
        fun passArgs(tripId: Int): String = "trip_tile_details/$tripId"
    }

    object ImportantNotes : Screen("important_notes/{tripId}") {
        fun passArgs(tripId: Int): String = "important_notes/$tripId"
    }

    object BlankNote :
        Screen("blank_note?titleEditNote={titleEditNote}&descriptionEditNote={descriptionEditNote}&idNote={idNote}&tripId={tripId}") {
        fun passArgs(
            titleEditNote: String?,
            descriptionEditNote: String?,
            idNote: String,
            tripId: String
        ): String {
            return "blank_note?" +
                    "${TripTileDetailsTag.TITLE_EDIT_NOTE}=${Uri.encode(titleEditNote.orEmpty())}" +
                    "&${TripTileDetailsTag.DESCRIPTION_EDIT_NOTE}=${Uri.encode(descriptionEditNote.orEmpty())}" +
                    "&${TripTileDetailsTag.ID_NOTE}=$idNote" +
                    "&${TripTileDetailsTag.TRIP_ID}=$tripId"
        }
    }
}