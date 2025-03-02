package com.example.wherenow.ui.app.triptiledetails.importantnotes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.wherenow.ui.components.WhereNowNotesTile

@Composable
fun ImportantNotes() {
    //TODO
    Column {
        WhereNowNotesTile(
            titleNotes = "First Note for preview",
            descriptionNotes = LoremIpsum(words = 50).values.joinToString(),
            onClick = {}
        )
    }
}