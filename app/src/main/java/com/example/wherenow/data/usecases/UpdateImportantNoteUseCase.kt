package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.repository.importantnotes.models.toItem

class UpdateImportantNoteUseCase internal constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(note: ImportantNoteItemData) =
        importantNotesRepository.updateNote(note = note.toItem())
}