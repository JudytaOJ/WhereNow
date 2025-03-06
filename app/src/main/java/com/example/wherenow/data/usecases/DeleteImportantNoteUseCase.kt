package com.example.wherenow.data.usecases

import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import javax.inject.Inject

class DeleteImportantNoteUseCase @Inject constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(id: Int) =
        importantNotesRepository.deleteNote(id)
}