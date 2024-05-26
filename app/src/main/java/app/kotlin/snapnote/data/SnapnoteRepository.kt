package app.kotlin.snapnote.data

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import app.kotlin.snapnote.worker.NotificationWorker
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.concurrent.TimeUnit


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

    fun makeNotification(context: Context, note: Note): String

    fun getWorkRequestId(id: Int): Flow<String?>
}

class LocalSnapnoteRepository(
    private val noteDao: NoteDao,
    context: Context
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

    private fun convertToLocalDateTime(date: String, time: String): LocalDateTime {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())

        val timeFormatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())

        val localDate: LocalDate = LocalDate.parse(date, dateFormatter)
        val localTime: LocalTime = LocalTime.parse(time, timeFormatter)

        return LocalDateTime.of(localDate, localTime)
    }

    private val workManager: WorkManager = WorkManager.getInstance(context)
    override fun makeNotification(context: Context, note: Note): String {
        val notificationTime: Long = convertToLocalDateTime(note.date, note.time)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val currentTime: Long = System.currentTimeMillis()

        val delay: Long = notificationTime - currentTime

        val data: Data = Data.Builder()
            .putString("note_title", note.title)
            .putString("note_body", note.body)
            .putInt("note_id", note.id)
            .build()

        val workRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueue(workRequest)

        return workRequest.id.toString()
    }

    override fun getWorkRequestId(id: Int): Flow<String?> = noteDao.getWorkRequestId(id = id)
}