package app.kotlin.snapnote.data

import kotlinx.coroutines.flow.Flow


interface SnapnoteRepository {
    suspend fun addNote(note: Note)

    suspend fun deleteNote(id: Int)

    fun getNoteById(id: Int): Flow<Note>

    fun getAllNotes(): Flow<List<Note>>

    fun getAllNotesSortedByTitle(): Flow<List<Note>>

    fun getAllNotesSortedByCompletion(): Flow<List<Note>>

    fun searchNote(keyword: String): Flow<List<Note>>

    suspend fun updateCompletion(id: Int)

    suspend fun pinNote(id: Int)

    suspend fun unpinNote()


    fun getAllFolders(): Flow<List<String>>
}

class LocalSnapnoteRepository(
    private val noteDao: NoteDao,
) : SnapnoteRepository {
    override suspend fun addNote(note: Note) = noteDao.addNote(note = note)

    override suspend fun deleteNote(id: Int) = noteDao.deleteNote(id = id)

    override fun getNoteById(id: Int) = noteDao.getNoteById(id = id)

    override fun getAllNotes() = noteDao.getAllNotes()

    override fun getAllNotesSortedByTitle() = noteDao.getAllNotesSortedByTitle()

    override fun getAllNotesSortedByCompletion() = noteDao.getALlNotesSortedByCompletion()

    override fun searchNote(keyword: String) = noteDao.searchNote(keyword = keyword)

    override suspend fun updateCompletion(id: Int) = noteDao.updateCompletion(id = id)

    override suspend fun pinNote(id: Int) = noteDao.pinNote(id = id)

    override suspend fun unpinNote() = noteDao.unpinNote()

    override fun getAllFolders() = noteDao.getAllFolders()
}