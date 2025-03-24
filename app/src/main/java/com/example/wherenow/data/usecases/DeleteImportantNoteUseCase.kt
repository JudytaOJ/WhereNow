package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository

class DeleteImportantNoteUseCase internal constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(id: Int) =
        importantNotesRepository.deleteNote(id)
}