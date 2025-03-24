package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData

class GetImportantNotesListUseCase internal constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(): List<ImportantNoteItemData> =
        importantNotesRepository.getNotesList().map {
            ImportantNoteItemData(
                title = it.title,
                description = it.description,
                id = it.id,
                tripId = it.tripId
            )
        }
}