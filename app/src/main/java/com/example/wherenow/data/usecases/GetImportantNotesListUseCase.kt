package com.example.wherenow.data.usecases

import com.example.wherenow.database.notes.Notes
import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImportantNotesListUseCase @Inject constructor(
    private val importantNotesRepository: ImportantNotesRepository
) {
    suspend operator fun invoke(): Flow<List<Notes>> =
        importantNotesRepository.getNotesList()
}