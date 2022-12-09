package app.date.carol.mynotes.database

import androidx.lifecycle.LiveData
import app.date.carol.mynotes.models.Note

class NotesRepository(private val noteDAO: NoteDAO) {

    val allNotes : LiveData<List<Note>> = noteDAO.getAllNotes()

    suspend fun insert(note: Note){
        noteDAO.insert(note)
    }

    suspend fun delete(note: Note){
        noteDAO.delete(note)
    }

    suspend fun update(note: Note){
        noteDAO.update(note.id, note.title, note.note )
    }
}