package app.kotlin.snapnote.ui.viewmodels

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

data class HomeScreenUiState(
    val notes: List<NoteUiModel> = emptyList(),
    val sortBy: String = "none",
    //
    val groupByFolder: Map<String, List<NoteUiModel>> = mapOf()
)

class HomeScreenViewModel(
    private val snapnoteRepository: SnapnoteRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeScreenUiState> =
        MutableStateFlow(value = HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        getAllNotes()
    }

    val sortingType: List<String> = mutableListOf(
        "none",
        "a-z",
        "folder-name",
        "completion"
    )

    fun changeSortType(sortBy: String) {
        _uiState.update { currentState ->
            currentState.copy(sortBy = sortBy)
        }
        getAllNotes()
    }


    private fun getAllNotes() {
        val currentSortingType: String = _uiState.value.sortBy

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val tempNotes: List<NoteUiModel>
                if (currentSortingType == sortingType[0]) {
                    tempNotes = snapnoteRepository
                        .getAllNotes()
                        .first()
                        .map { noteToNoteUiModel(note = it) }

                    _uiState.update { currentState ->
                        currentState.copy(
                            notes = tempNotes,
                            groupByFolder = emptyMap()
                        )
                    }
                } else if (currentSortingType == sortingType[1]) {
                    tempNotes = snapnoteRepository
                        .getAllNotesSortedByTitle()
                        .first()
                        .map { noteToNoteUiModel(note = it) }

                    _uiState.update { currentState ->
                        currentState.copy(
                            notes = tempNotes,
                            groupByFolder = emptyMap()
                        )
                    }
                } else if (currentSortingType == sortingType[3]) {
                    tempNotes = snapnoteRepository
                        .getAllNotesSortedByCompletion()
                        .first()
                        .map { noteToNoteUiModel(note = it) }

                    _uiState.update { currentState ->
                        currentState.copy(notes = tempNotes)
                    }
                } else {
                    val tempGroupByFolder: Map<String, List<NoteUiModel>> = snapnoteRepository
                        .getAllNotes()
                        .first()
                        .map { noteToNoteUiModel(note = it) }
                        .groupBy { it.folderName }

                    _uiState.update { currentState ->
                        currentState.copy(groupByFolder = tempGroupByFolder)
                    }
                }
            }
        }
    }

    fun updateCompletionStatus(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                snapnoteRepository.updateCompletion(id = id)
            }
            getAllNotes()
        }
    }


    fun pinOrUnpin(id: Int) {
        viewModelScope.launch {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val tempNote: Note = snapnoteRepository
                        .getNoteById(id = id)
                        .first()
                    if (tempNote.isPinned)
                        snapnoteRepository.unpinNote()
                    else
                        snapnoteRepository.pinNote(id = id)
                }
            }
            getAllNotes()
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                snapnoteRepository.deleteNote(id = id)
            }
            getAllNotes()
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: SnapnoteApplication =
                    (this[APPLICATION_KEY] as SnapnoteApplication)
                val snapnoteRepository: SnapnoteRepository =
                    application.appContainer.snapnoteRepository
                HomeScreenViewModel(
                    snapnoteRepository = snapnoteRepository
                )
            }
        }
    }
}