package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.repository.importantnotes.models.toItem

class SaveImportantNoteUseCase internal constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(note: ImportantNoteItemData) =
        importantNotesRepository.saveNote(note.toItem())
}