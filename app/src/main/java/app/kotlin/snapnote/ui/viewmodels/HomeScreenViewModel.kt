package app.kotlin.snapnote.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

data class NoteShown(
    val originalIndex: Int,
    val isDone: Boolean = false,
    var title: String = "Unknown title",
    val body: String = "This is a sample note. To make it very long, I will add \"very long very long very long\"",
    private val originalDate: LocalDate = LocalDate.now(),
    private val originalTime: LocalTime = LocalTime.now()
        .plusMinutes(1)
        .withSecond(0)
        .withNano(0),
    var isPinned: Boolean = false
) {
    companion object {
        private val timeFormatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
    }

    val time: String
        get() = originalTime.format(timeFormatter)

    val date: String
        get() = originalDate.format(dateFormatter)
}

data class HomeScreenUiState(
    val notes: List<NoteShown> = listOf()
)

class HomeScreenViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(value = HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val dumpData: List<NoteShown> = listOf(
        NoteShown(
            isDone = false,
            title = "Shopping List",
            body = "Buy milk, eggs, and bread.",
            originalDate = LocalDate.of(2024, 5, 17),
            originalTime = LocalTime.of(9, 0),
            isPinned = false,
            originalIndex = 0
        ),
        NoteShown(
            isDone = true,
            title = "Workout Plan",
            body = "Run 5km and do 30 minutes of strength training.",
            originalDate = LocalDate.of(2024, 5, 16),
            originalTime = LocalTime.of(7, 30),
            isPinned = false,
            originalIndex = 1
        ),
        NoteShown(
            isDone = false,
            title = "Project Meeting",
            body = "Discuss project milestones and deliverables.",
            originalDate = LocalDate.of(2024, 5, 18),
            originalTime = LocalTime.of(14, 0),
            isPinned = false,
            originalIndex = 2
        ),
        NoteShown(
            isDone = true,
            title = "Doctor's Appointment",
            body = "Annual health check-up at the clinic.",
            originalDate = LocalDate.of(2024, 5, 15),
            originalTime = LocalTime.of(10, 15),
            isPinned = false,
            originalIndex = 3
        ),
        NoteShown(
            isDone = false,
            title = "Dinner with Friends",
            body = "Meet at the new Italian restaurant.",
            originalDate = LocalDate.of(2024, 5, 20),
            originalTime = LocalTime.of(19, 0),
            isPinned = false,
            originalIndex = 4
        ),
        NoteShown(
            isDone = false,
            title = "Read Book",
            body = "Finish reading 'Kotlin for Beginners'.",
            originalDate = LocalDate.of(2024, 5, 21),
            originalTime = LocalTime.of(16, 0),
            isPinned = false,
            originalIndex = 5
        ),
        NoteShown(
            isDone = true,
            title = "Grocery Shopping",
            body = "Buy vegetables, fruits, and snacks.",
            originalDate = LocalDate.of(2024, 5, 14),
            originalTime = LocalTime.of(11, 0),
            isPinned = false,
            originalIndex = 6
        ),
        NoteShown(
            isDone = false,
            title = "Team Presentation",
            body = "Prepare slides for the upcoming presentation.",
            originalDate = LocalDate.of(2024, 5, 19),
            originalTime = LocalTime.of(13, 0),
            isPinned = false,
            originalIndex = 7
        ),
        NoteShown(
            isDone = true,
            title = "House Cleaning",
            body = "Clean the living room and kitchen.",
            originalDate = LocalDate.of(2024, 5, 13),
            originalTime = LocalTime.of(15, 0),
            isPinned = false,
            originalIndex = 8
        )
    )

    init {
        _uiState.update { currentState ->
            currentState.copy(notes = dumpData)
        }
    }

    private fun updateElementNote(position: Int, newNote: NoteShown) {
        val tempNotes: MutableList<NoteShown> = _uiState.value.notes.toMutableList()
        tempNotes[position] = newNote
        _uiState.update { currentState ->
            currentState.copy(
                notes = tempNotes.toList()
            )
        }
    }

    fun updateCompletionStatus(position: Int) {
        val currentNote: NoteShown = _uiState.value.notes[position]
        val updatedNote: NoteShown = currentNote.copy(isDone = !currentNote.isDone)

        updateElementNote(
            position = position,
            newNote = updatedNote
        )
    }

    fun pinOrUnpin(position: Int) {
        val tempNotes: MutableList<NoteShown> = _uiState.value.notes.toMutableList()
        if (!tempNotes[position].isPinned) {
            tempNotes.forEachIndexed { index, noteShown ->
                noteShown.isPinned = (index == position)
            }
            val pinnedNote: NoteShown = tempNotes.removeAt(index = position)
            tempNotes.add(
                index = 0,
                element = pinnedNote
            )
        } else {
            tempNotes[position].isPinned = false
            tempNotes.sortBy {
                it.originalIndex
            }
        }

        _uiState.update { currentState ->
            currentState.copy(notes = tempNotes.toList())
        }
    }

    fun deleteNote(position: Int) {
        val tempNotes: MutableList<NoteShown> = _uiState.value.notes.toMutableList()
        tempNotes.removeAt(index = position)
        _uiState.update { currentState ->
            currentState.copy(notes = tempNotes)
        }
    }
}