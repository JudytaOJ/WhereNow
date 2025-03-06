package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.models.ImportantNoteItemData
import com.example.wherenow.repository.importantnotes.models.toItem
import javax.inject.Inject

class SaveImportantNoteUseCase @Inject constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(note: ImportantNoteItemData) =
        importantNotesRepository.saveNote(note.toItem())
}