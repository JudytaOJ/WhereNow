package com.example.wherenow.repository.importantnotes

import com.example.wherenow.database.notes.NoteDatabase
import com.example.wherenow.database.notes.Notes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ImportantNotesRepository {
    suspend fun saveNote(note: Notes)
    suspend fun getNotesList(): Flow<List<Notes>>
    suspend fun deleteNote(id: Int)
}

class ImportantNotesRepositoryImpl @Inject constructor(
    private val db: NoteDatabase
) : ImportantNotesRepository {

    override suspend fun saveNote(note: Notes) = db.dao().saveNote(notes = note)

    override suspend fun getNotesList(): Flow<List<Notes>> = db.dao().getAllNotes()

    override suspend fun deleteNote(id: Int) = db.dao().deleteNote(id = id)
}