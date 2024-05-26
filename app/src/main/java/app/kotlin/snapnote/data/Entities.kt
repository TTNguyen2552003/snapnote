package app.kotlin.snapnote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val folderName: String = "Uncategorized",
    val title: String = "",
    val body: String = "",
    val date: String = "",
    val time: String = "",
    val isPinned: Boolean = false,
    val isDone: Boolean = false,
    val workRequestId: String? = null
)