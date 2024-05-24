package app.kotlin.snapnote.data.models

import app.kotlin.snapnote.data.Note

data class NoteUiModel(
    val originalIndex: Int = 0,
    val folderName: String = "Uncategorized",
    val title: String = "",
    val body: String = "",
    val date: String = "",
    val time: String = "",
    val isPinned: Boolean = false,
    val isDone: Boolean = false
)

fun noteToNoteUiModel(note: Note): NoteUiModel {
    return NoteUiModel(
        originalIndex = note.id,
        folderName = note.folderName,
        title = note.title,
        body = note.body,
        date = note.date,
        time = note.time,
        isPinned = note.isPinned,
        isDone = note.isDone
    )
}
