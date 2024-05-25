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
import app.kotlin.snapnote.ui.views.MAX_BODY_LENGTH
import app.kotlin.snapnote.ui.views.MAX_TITLE_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CreateNoteScreenUiState(
    val currentFolderName: String = "Uncategorized",
    val title: String = "",
    val body: String = "",
    val date: String = "Date",
    val time: String = "Time",
    val isReminderSet: Boolean = false
)

class CreateNoteScreenViewModel(
    private val snapnoteRepository: SnapnoteRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CreateNoteScreenUiState> =
        MutableStateFlow(CreateNoteScreenUiState())

    val uiState: StateFlow<CreateNoteScreenUiState> = _uiState.asStateFlow()

    lateinit var folders: Set<String>

    init {
        getAllFolder()
        _uiState.update { currentState ->
            currentState.copy(
                currentFolderName = "Uncategorized",
                title = "",
                body = "",
                date = "Date",
                time = "Time",
                isReminderSet = false
            )
        }
    }

    private fun getAllFolder() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                folders = snapnoteRepository
                    .getAllFolders()
                    .first()
                    .toSet()
            }
        }
    }


    fun updateTitle(newTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = if (newTitle.length > MAX_TITLE_LENGTH)
                    newTitle.substring(
                        startIndex = 0,
                        endIndex = MAX_TITLE_LENGTH
                    )
                else
                    newTitle
            )
        }
    }

    fun updateBody(newBody: String) {
        _uiState.update { currentState ->
            currentState.copy(
                body = if (newBody.length > MAX_BODY_LENGTH)
                    newBody.substring(
                        startIndex = 0,
                        endIndex = MAX_BODY_LENGTH
                    )
                else newBody
            )
        }
    }

    fun updateCurrentNewFolderName(newFolderName: String) {
        _uiState.update { currentState ->
            currentState.copy(currentFolderName = newFolderName)
        }
    }

    fun pressOnSwitch() {
        _uiState.update { currentState ->
            currentState.copy(
                isReminderSet = !currentState.isReminderSet
            )
        }

        if (!uiState.value.isReminderSet) {
            _uiState.update { currentState ->
                currentState.copy(
                    date = "Date",
                    time = "Time"
                )
            }
        }
    }

    fun updateDate(newDate: String) {
        _uiState.update { currentState ->
            currentState.copy(date = newDate)
        }
    }

    fun updateTime(newTime: String) {
        _uiState.update { currentState ->
            currentState.copy(time = newTime)
        }
    }

    fun saveNote() {
        val currentState: CreateNoteScreenUiState = _uiState.value
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                snapnoteRepository.addNote(
                    note = Note(
                        folderName = currentState.currentFolderName,
                        title = currentState.title,
                        body = currentState.body,
                        date = if (currentState.date != "Date")
                            currentState.date
                        else
                            "",
                        time = if (currentState.time != "Time")
                            currentState.time
                        else
                            ""
                    )
                )
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
                CreateNoteScreenViewModel(
                    snapnoteRepository = snapnoteRepository
                )
            }
        }
    }
}