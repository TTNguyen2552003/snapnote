package app.kotlin.snapnote.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.kotlin.snapnote.SnapnoteApplication
import app.kotlin.snapnote.data.Note
import app.kotlin.snapnote.data.SnapnoteRepository
import app.kotlin.snapnote.data.models.NoteUiModel
import app.kotlin.snapnote.data.models.noteToNoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Highlights(
    val title: AnnotatedString = AnnotatedString(text = ""),
    val body: AnnotatedString = AnnotatedString(text = "")
)

data class SearchingScreenUiState(
    val results: List<NoteUiModel> = listOf(),
    val highlights: List<Highlights> = listOf(),
    val keyword: String = ""
)

class SearchingScreenViewModel(
    private val snapnoteRepository: SnapnoteRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchingScreenUiState> =
        MutableStateFlow(value = SearchingScreenUiState())
    val uiState: StateFlow<SearchingScreenUiState> = _uiState.asStateFlow()

    init {
        searchNote()
    }

    private fun <T> debounce(
        waitMs: Long = 150L,
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

    private val debouncedSearchNote = debounce<String>(
        waitMs = 500L,
        coroutineScope = viewModelScope
    ) {
        searchNote()
    }

    fun updateKeyword(newKeyword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                keyword = newKeyword,
                results = listOf()
            )
        }
        debouncedSearchNote(newKeyword)
    }

    private fun searchNote() {
        val keyword: String = _uiState.value.keyword

        if (keyword.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    results = listOf()
                )
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            results = snapnoteRepository
                                .searchNote(keyword = keyword)
                                .first()
                                .map {
                                    noteToNoteUiModel(note = it)
                                }
                        )
                    }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        highlights = currentState.results.map {
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
                    )
                }
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

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                snapnoteRepository.deleteNote(id = id)
                searchNote()
            }
        }
    }

    fun pinOrUnpin(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val tempNote: Note = snapnoteRepository
                    .getNoteById(id = id)
                    .first()

                if (tempNote.isPinned)
                    snapnoteRepository.unpinNote()
                else
                    snapnoteRepository.pinNote(id = id)
                searchNote()
            }
        }
    }

    fun updateCompletionStatus(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                snapnoteRepository.updateCompletion(id = id)
                searchNote()
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: SnapnoteApplication =
                    (this[APPLICATION_KEY] as SnapnoteApplication)
                val snapnoteRepository: SnapnoteRepository =
                    application.appContainer.snapnoteRepository
                SearchingScreenViewModel(
                    snapnoteRepository = snapnoteRepository
                )
            }
        }
    }
}