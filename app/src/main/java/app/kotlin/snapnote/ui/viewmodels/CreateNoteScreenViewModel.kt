package app.kotlin.snapnote.ui.viewmodels

import androidx.lifecycle.ViewModel
import app.kotlin.snapnote.ui.views.MAX_TITLE_LENGTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateNoteScreenUiState(
    val currentFolderName: String = "Uncategorized",
    val title: String = "",
    val body: String = "",
    val isReminderSet: Boolean = false
)

class CreateNoteScreenViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<CreateNoteScreenUiState> =
        MutableStateFlow(CreateNoteScreenUiState())

    val uiState: StateFlow<CreateNoteScreenUiState> = _uiState.asStateFlow()

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
                body = if (newBody.length > MAX_TITLE_LENGTH)
                    newBody.substring(
                        startIndex = 0,
                        endIndex = MAX_TITLE_LENGTH
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
    }
}