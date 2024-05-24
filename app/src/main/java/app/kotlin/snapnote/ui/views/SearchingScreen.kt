package app.kotlin.snapnote.ui.views

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.kotlin.snapnote.R
import app.kotlin.snapnote.data.models.NoteUiModel
import app.kotlin.snapnote.ui.theme.bodySmall
import app.kotlin.snapnote.ui.theme.errorDark
import app.kotlin.snapnote.ui.theme.errorLight
import app.kotlin.snapnote.ui.theme.labelSmall
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.outlineDark
import app.kotlin.snapnote.ui.theme.outlineLight
import app.kotlin.snapnote.ui.theme.outlineVariantDark
import app.kotlin.snapnote.ui.theme.outlineVariantLight
import app.kotlin.snapnote.ui.theme.primaryDark
import app.kotlin.snapnote.ui.theme.primaryLight
import app.kotlin.snapnote.ui.theme.surfaceContainerDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighestDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighestLight
import app.kotlin.snapnote.ui.theme.surfaceContainerLight
import app.kotlin.snapnote.ui.theme.titleSmall
import app.kotlin.snapnote.ui.viewmodels.Highlights
import app.kotlin.snapnote.ui.viewmodels.SearchingScreenUiState
import app.kotlin.snapnote.ui.viewmodels.SearchingScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchingScreen(
    isDarkMode: Boolean = false,
    navController: NavController,
    searchingScreenViewModel: SearchingScreenViewModel = viewModel(
        factory = SearchingScreenViewModel.factory
    )
) {
    val searchingScreenUiState: State<SearchingScreenUiState> = searchingScreenViewModel
        .uiState
        .collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(insets = WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))

        @Composable
        fun Searching() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    )
                    .padding(
                        end = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                @Composable
                fun SearchBar() {
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(weight = 1f)
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = if (isDarkMode)
                                        surfaceContainerDark
                                    else
                                        surfaceContainerLight,
                                    cornerRadius = CornerRadius(x = 8.dp.toPx())
                                )
                            }
                            .padding(
                                start = 16.dp, end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        @Composable
                        fun Head() {
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(weight = 1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_icon),
                                    contentDescription = "search icon",
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(height = 24.dp),
                                    tint = if (isDarkMode)
                                        outlineDark
                                    else
                                        outlineLight
                                )

                                val focusRequester: FocusRequester = remember { FocusRequester() }
                                val focusManager: FocusManager = LocalFocusManager.current

                                LaunchedEffect(Unit) {
                                    focusRequester.requestFocus()
                                    focusManager.moveFocus(FocusDirection.Enter)
                                }
                                BasicTextField(
                                    value = searchingScreenUiState.value.keyword,
                                    onValueChange = {
                                        searchingScreenViewModel.updateKeyword(newKeyword = it)
                                    },
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .weight(weight = 1f)
                                        .focusRequester(focusRequester = focusRequester),
                                    keyboardOptions = KeyboardOptions
                                        .Default
                                        .copy(imeAction = ImeAction.Search),
                                    singleLine = true,
                                    cursorBrush = SolidColor(
                                        value = if (isDarkMode)
                                            outlineDark
                                        else
                                            outlineLight
                                    ),
                                    decorationBox = { innerTextField ->
                                        if (searchingScreenUiState.value.keyword.isEmpty()) {
                                            // Display placeholder when text is empty
                                            Text(
                                                text = stringResource(id = R.string.place_holder_search_bar),
                                                style = bodySmall.notScale(),
                                                color = if (isDarkMode)
                                                    outlineDark
                                                else
                                                    outlineLight
                                            )
                                        }
                                        innerTextField() // This will display the actual text field
                                    }
                                )
                            }
                        }
                        Head()

//                        Trailing Icon
                        if (searchingScreenUiState.value.keyword.isNotEmpty())
                            Icon(
                                painter = painterResource(id = R.drawable.cancel_icon),
                                contentDescription = "clear search",
                                modifier = Modifier
                                    .width(width = 24.dp)
                                    .height(height = 24.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                searchingScreenViewModel.updateKeyword(newKeyword = "")
                                            }
                                        )
                                    },
                                tint = if (isDarkMode)
                                    outlineDark
                                else
                                    outlineLight
                            )
                    }
                }
                SearchBar()

//                Cancel searching button
                Text(
                    text = stringResource(id = R.string.button_label_cancel),
                    style = bodySmall.notScale(),
                    color = if (isDarkMode)
                        outlineDark
                    else
                        outlineLight,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                navController.navigate(
                                    route = Destination.MainScreen.route
                                ) {
                                    popUpTo(route = Destination.MainScreen.route) {
                                        inclusive = false
                                    }
                                }
                            }
                        )
                    }
                )
            }
        }
        Searching()

        Spacer(modifier = Modifier.height(height = 16.dp))

        @Composable
        fun Results() {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    ),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                @Composable
                fun Note(
                    note: NoteUiModel,
                    highlights: Highlights,
                    updateCompletionStatus: () -> Unit,
                    pinOrUnpinNote: () -> Unit,
                    deleteNote: () -> Unit
                ) {
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = if (note.isDone) {
                                        if (isDarkMode)
                                            surfaceContainerHighestDark
                                        else
                                            surfaceContainerHighestLight
                                    } else {
                                        if (isDarkMode)
                                            surfaceContainerDark
                                        else
                                            surfaceContainerLight
                                    },
                                    cornerRadius = CornerRadius(x = 8.dp.toPx())
                                )
                            }
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                start = 8.dp,
                                end = 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                    ) {
                        @Composable
                        fun CheckBoxContainer() {
                            Box(
                                modifier = Modifier
                                    .width(width = 48.dp)
                                    .height(height = 48.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                updateCompletionStatus()
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (!note.isDone)
                                    Icon(
                                        painter = painterResource(id = R.drawable.checkbox_icon),
                                        contentDescription = "check box",
                                        modifier = Modifier
                                            .width(width = 24.dp)
                                            .height(height = 24.dp),
                                        tint = if (isDarkMode)
                                            primaryDark
                                        else
                                            primaryLight
                                    )
                                else
                                    Icon(
                                        painter = painterResource(id = R.drawable.checkbox_selected_icon),
                                        contentDescription = "check box",
                                        modifier = Modifier
                                            .width(width = 24.dp)
                                            .height(height = 24.dp),
                                        tint = if (isDarkMode)
                                            outlineVariantDark
                                        else outlineVariantLight
                                    )
                            }
                        }
                        CheckBoxContainer()

                        @Composable
                        fun Content() {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                            ) {
//                                            Title
                                Text(
                                    text = highlights.title,
                                    style = titleSmall.notScale(),
                                    color = if (!note.isDone) {
                                        if (isDarkMode)
                                            onSurfaceDark
                                        else
                                            onSurfaceLight
                                    } else {
                                        if (isDarkMode)
                                            outlineVariantDark
                                        else
                                            outlineVariantLight
                                    }
                                )

//                                            Body
                                Text(
                                    text = highlights.body,
                                    style = bodySmall.notScale(),
                                    color = if (!note.isDone) {
                                        if (isDarkMode)
                                            onSurfaceDark
                                        else
                                            onSurfaceLight
                                    } else {
                                        if (isDarkMode)
                                            outlineVariantDark
                                        else
                                            outlineVariantLight
                                    },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                @Composable
                                fun Reminder() {
                                    Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.reminder_icon),
                                            contentDescription = "reminder",
                                            modifier = Modifier
                                                .width(width = 16.dp)
                                                .height(height = 16.dp),
                                            tint = if (!note.isDone) {
                                                if (isDarkMode)
                                                    onSurfaceDark
                                                else
                                                    onSurfaceLight
                                            } else {
                                                if (isDarkMode)
                                                    outlineVariantDark
                                                else
                                                    outlineVariantLight
                                            }
                                        )

                                        @Composable
                                        fun DateAndTime() {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    space = 4.dp
                                                )
                                            ) {
//                                                            Date
                                                Text(
                                                    text = note.date,
                                                    style = labelSmall.notScale(),
                                                    color = if (!note.isDone) {
                                                        if (isDarkMode)
                                                            onSurfaceDark
                                                        else
                                                            onSurfaceLight
                                                    } else {
                                                        if (isDarkMode)
                                                            outlineVariantDark
                                                        else
                                                            outlineVariantLight
                                                    }
                                                )

//                                                            Time
                                                Text(
                                                    text = note.time,
                                                    style = labelSmall.notScale(),
                                                    color = if (!note.isDone) {
                                                        if (isDarkMode)
                                                            onSurfaceDark
                                                        else
                                                            onSurfaceLight
                                                    } else {
                                                        if (isDarkMode)
                                                            outlineVariantDark
                                                        else
                                                            outlineVariantLight
                                                    }
                                                )
                                            }
                                        }
                                        DateAndTime()
                                    }
                                }
                                Reminder()
                            }
                        }
                        Content()

                        @Composable
                        fun TrailingInteraction() {
                            Column(
                                modifier = Modifier.height(height = 68.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
//                                Pin icon
                                Icon(
                                    painter = painterResource(
                                        id = if (note.isPinned)
                                            R.drawable.pin_filled_icon
                                        else
                                            R.drawable.pin_icon
                                    ),
                                    contentDescription = "pin note",
                                    modifier = Modifier
                                        .width(width = 16.dp)
                                        .height(16.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    pinOrUnpinNote()
                                                }
                                            )
                                        },
                                    tint = if (isDarkMode)
                                        primaryDark
                                    else
                                        primaryLight
                                )

//                                Delete icon
                                Icon(
                                    painter = painterResource(id = R.drawable.delete_icon),
                                    contentDescription = "delete note",
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(16.dp)
                                        .pointerInput(Unit) {
                                            deleteNote()
                                        },
                                    tint = if (isDarkMode)
                                        errorDark
                                    else
                                        errorLight
                                )
                            }
                        }
                        TrailingInteraction()
                    }
                }

                searchingScreenUiState.value.results.forEachIndexed { index, result ->
                    item {
                        Note(
                            note = result,
                            highlights = searchingScreenUiState.value.highlights[index],
                            updateCompletionStatus = {
                                searchingScreenViewModel
                                    .updateCompletionStatus(
                                        id = result.originalIndex
                                    )
                            },
                            pinOrUnpinNote = {
                                searchingScreenViewModel
                                    .pinOrUnpin(
                                        id = result.originalIndex
                                    )
                            },
                            deleteNote = {
                                searchingScreenViewModel
                                    .deleteNote(
                                        id = result.originalIndex
                                    )
                            }
                        )
                    }
                }
            }
        }
        Results()
    }
}