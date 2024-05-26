package app.kotlin.snapnote.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): Flow<Note>

    @Query("SELECT * FROM notes ORDER BY isPinned DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, title ASC")
    fun getAllNotesSortedByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY isPinned DESC, isDone ASC")
    fun getALlNotesSortedByCompletion(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE LOWER(title) LIKE '%' || LOWER(:keyword) || '%' OR LOWER(body) LIKE '%' || LOWER(:keyword) || '%' ORDER BY isPinned DESC")
    fun searchNote(keyword: String): Flow<List<Note>>

    @Query("SELECT folderName from notes")
    fun getAllFolders(): Flow<List<String>>

    @Query("UPDATE notes SET isDone = CASE WHEN isDone = 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun updateCompletion(id: Int)

    @Query("UPDATE notes SET isPinned = CASE WHEN id = :id THEN TRUE ELSE FALSE END")
    suspend fun pinNote(id: Int)

    @Query("UPDATE notes SET isPinned = FALSE")
    suspend fun unpinNote()

    @Query("SELECT workRequestId FROM notes WHERE id = :id")
    fun getWorkRequestId(id: Int): Flow<String?>
}