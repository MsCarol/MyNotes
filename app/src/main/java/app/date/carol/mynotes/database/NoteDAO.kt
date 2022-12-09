package app.date.carol.mynotes.database

import androidx.lifecycle.LiveData
import androidx.room.*
import app.date.carol.mynotes.models.Note


@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("UPDATE notes_table Set title = :title, note = :note WHERE id = :id")
    suspend fun update(id: Int?, title: String?, note: String?)

    @Delete
    suspend fun delete(note: Note)

    @Query("delete from notes_table")
    fun deleteAllNotes()

    @Query("select * from notes_table order by id ASC")
    fun getAllNotes() : LiveData<List<Note>>

}