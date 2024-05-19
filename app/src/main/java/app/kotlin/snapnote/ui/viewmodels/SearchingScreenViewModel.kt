package app.kotlin.snapnote.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class Highlights(
    val title: AnnotatedString = AnnotatedString(text = ""),
    val body: AnnotatedString = AnnotatedString(text = "")
)

data class SearchingScreenUiState(
    val results: List<NoteShown> = listOf(),
    val highlights: List<Highlights> = listOf(),
    val keyword: String = ""
)

class SearchingScreenViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<SearchingScreenUiState> =
        MutableStateFlow(value = SearchingScreenUiState())
    val uiState: StateFlow<SearchingScreenUiState> = _uiState.asStateFlow()

    private val source: List<NoteShown> = listOf(
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
        findNote()
    }

    private fun <T> debounce(
        waitMs: Long = 500L,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null

        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

    private val debouncedFindNote = debounce<String>(
        waitMs = 500L,
        coroutineScope = viewModelScope
    ) {
        findNote()
    }

    fun updateKeyword(newKeyword: String) {
        _uiState.update { currentState ->
            currentState.copy(keyword = newKeyword)
        }
        debouncedFindNote(newKeyword)
    }

    private fun findNote() {
        viewModelScope.launch {
            val keyword: String = _uiState.value.keyword
            val filteredResults: List<NoteShown> = source.filter {
                it.title.contains(
                    other = keyword,
                    ignoreCase = true
                ) || it.body.contains(
                    other = keyword,
                    ignoreCase = true
                )
            }
            val highlightedResults: List<Highlights> = filteredResults.map {
                Highlights(
                    title = highlightText(
                        text = it.title,
                        keyword = keyword
                    ),
                    body = highlightText(
                        text = it.body,
                        keyword = keyword
                    )
                )
            }
            _uiState.update { currentState ->
                currentState.copy(
                    results = filteredResults,
                    highlights = highlightedResults
                )
            }
        }
    }

    private fun highlightText(text: String, keyword: String): AnnotatedString {
        if (keyword.isEmpty()) return AnnotatedString(text)
        val builder = AnnotatedString.Builder()
        var startIndex = 0
        while (startIndex < text.length) {
            val index: Int = text.indexOf(
                keyword,
                startIndex,
                ignoreCase = true
            )
            if (index == -1) {
                builder.append(text.substring(startIndex))
                break
            }
            builder.append(text.substring(startIndex, index))
            builder.withStyle(
                SpanStyle(
                    color = Color(color = 0xFFDC3545),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(text.substring(index, index + keyword.length))
            }
            startIndex = index + keyword.length
        }
        return builder.toAnnotatedString()
    }
}