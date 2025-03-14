package com.example.wherenow.repository.importantnotes

import com.example.wherenow.database.notes.NoteDatabase
import com.example.wherenow.database.notes.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ImportantNotesRepository {
    suspend fun saveNote(note: Notes)
    suspend fun getNotesList(): List<Notes>
    suspend fun deleteNote(id: Int)
    suspend fun updateNote(note: Notes)
}

class ImportantNotesRepositoryImpl @Inject constructor(
    private val db: NoteDatabase,
    private val dispatchers: Dispatchers
) : ImportantNotesRepository {

    override suspend fun saveNote(note: Notes) = db.dao().saveNote(notes = note)

    override suspend fun getNotesList(): List<Notes> =
        withContext(dispatchers.IO) {
            db.dao().getAllNotes()
        }

    override suspend fun deleteNote(id: Int) = db.dao().deleteNote(id = id)

    override suspend fun updateNote(note: Notes) = db.dao().updateNote(note = note)
}