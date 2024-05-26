package app.kotlin.snapnote.ui.views

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.kotlin.snapnote.Destination
import app.kotlin.snapnote.R
import app.kotlin.snapnote.data.models.NoteUiModel
import app.kotlin.snapnote.ui.theme.bodySmall
import app.kotlin.snapnote.ui.theme.errorDark
import app.kotlin.snapnote.ui.theme.errorLight
import app.kotlin.snapnote.ui.theme.labelSmall
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onPrimaryContainerDark
import app.kotlin.snapnote.ui.theme.onPrimaryContainerLight
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.outlineDark
import app.kotlin.snapnote.ui.theme.outlineLight
import app.kotlin.snapnote.ui.theme.outlineVariantDark
import app.kotlin.snapnote.ui.theme.outlineVariantLight
import app.kotlin.snapnote.ui.theme.primaryContainerDark
import app.kotlin.snapnote.ui.theme.primaryContainerLight
import app.kotlin.snapnote.ui.theme.primaryDark
import app.kotlin.snapnote.ui.theme.primaryLight
import app.kotlin.snapnote.ui.theme.surfaceContainerDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighestDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighestLight
import app.kotlin.snapnote.ui.theme.surfaceContainerLight
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight
import app.kotlin.snapnote.ui.theme.titleLarge
import app.kotlin.snapnote.ui.theme.titleMedium
import app.kotlin.snapnote.ui.theme.titleSmall
import app.kotlin.snapnote.ui.viewmodels.HomeScreenUiState
import app.kotlin.snapnote.ui.viewmodels.HomeScreenViewModel


data class MainScreenDestination(
    val index: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val description: String = ""
)

@Composable
fun MainScreen(
    isDarkMode: Boolean = false,
    navController: NavController,
    saveAndSwitchUiMode: () -> Unit
) {
    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finish()
    }

    var selectedMainScreenDestination: Int by remember {
        mutableIntStateOf(value = 0)
    }

    val mainScreenDestinations: List<MainScreenDestination> = listOf(
        MainScreenDestination(
            index = 0,
            selectedIcon = R.drawable.home_filled_icon,
            unselectedIcon = R.drawable.home_icon,
            description = "Home Tab"
        ),
        MainScreenDestination(
            index = 1,
            selectedIcon = R.drawable.info_filled_icon,
            unselectedIcon = R.drawable.info_icon,
            description = "Info Tab"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    color = if (isDarkMode)
                        surfaceDark
                    else
                        surfaceLight
                )
            }
            .windowInsetsPadding(insets = WindowInsets.statusBars)
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))

        @Composable
        fun TopBarNavigation() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                mainScreenDestinations.forEachIndexed { index, mainScreenDestination ->
                    Icon(
                        painter = if (selectedMainScreenDestination == mainScreenDestination.index)
                            painterResource(id = mainScreenDestination.selectedIcon)
                        else
                            painterResource(id = mainScreenDestination.unselectedIcon),
                        contentDescription = mainScreenDestination.description,
                        tint = if (selectedMainScreenDestination == mainScreenDestination.index) {
                            if (isDarkMode)
                                primaryContainerDark
                            else
                                primaryContainerLight
                        } else {
                            if (isDarkMode) {
                                outlineDark
                            } else {
                                outlineLight
                            }
                        },
                        modifier = Modifier
                            .width(width = 40.dp)
                            .height(height = 40.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        selectedMainScreenDestination = mainScreenDestination.index
                                    }
                                )
                            }
                    )
                    if (index < mainScreenDestinations.size - 1)
                        Spacer(modifier = Modifier.width(80.dp))
                }
            }
        }
        TopBarNavigation()

        Spacer(modifier = Modifier.height(16.dp))

        @Composable
        fun HomeScreen(
            homeScreenViewModel: HomeScreenViewModel = viewModel(
                factory = HomeScreenViewModel.factory
            )
        ) {
            LaunchedEffect(Unit) {
                homeScreenViewModel.clearState()
            }

            val homeScreenUiState: State<HomeScreenUiState> = homeScreenViewModel
                .uiState
                .collectAsState()

            if (homeScreenUiState.value.notes.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {

                    @Composable
                    fun NoteListContainer() {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp
                                ),
                            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                        ) {
                            @Composable
                            fun SearchBar() {
                                Row(
                                    modifier = Modifier
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
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(
                                            start = 16.dp,
                                            top = 8.dp,
                                            bottom = 8.dp
                                        )
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    navController.navigate(
                                                        route = Destination.SearchingScreen.route
                                                    )
                                                }
                                            )
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    @Composable
                                    fun Head() {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.search_icon),
                                                contentDescription = "search",
                                                modifier = Modifier
                                                    .width(width = 24.dp)
                                                    .height(height = 24.dp),
                                                tint = if (isDarkMode)
                                                    outlineDark
                                                else
                                                    outlineLight
                                            )

                                            Text(
                                                text = stringResource(id = R.string.place_holder_search_bar),
                                                style = bodySmall.notScale(),
                                                color = if (isDarkMode)
                                                    outlineDark.copy(alpha = 0.3f)
                                                else
                                                    outlineLight.copy(alpha = 0.3f),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    Head()
                                }
                            }
                            SearchBar()

                            @Composable
                            fun SortByChip() {
                                var isSortByChipExpanded: Boolean by remember {
                                    mutableStateOf(value = false)
                                }

                                Box(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .align(alignment = Alignment.End)
                                ) {
                                    if (homeScreenUiState.value.sortBy == "none") {
                                        Icon(
                                            painter = painterResource(id = R.drawable.sort_icon),
                                            contentDescription = "sort button",
                                            tint = if (isDarkMode)
                                                outlineDark
                                            else
                                                outlineLight,
                                            modifier = Modifier
                                                .width(width = 24.dp)
                                                .height(height = 24.dp)
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onPress = {
                                                            isSortByChipExpanded = true
                                                        }
                                                    )
                                                }
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(shape = RoundedCornerShape(size = 4.dp))
                                                .drawBehind {
                                                    drawRoundRect(
                                                        color = if (isDarkMode)
                                                            surfaceContainerDark
                                                        else
                                                            surfaceContainerLight,
                                                        cornerRadius = CornerRadius(x = 4.dp.toPx())
                                                    )
                                                }
                                                .padding(
                                                    top = 4.dp,
                                                    bottom = 4.dp,
                                                    start = 8.dp,
                                                    end = 8.dp
                                                )
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onPress = {
                                                            isSortByChipExpanded = true
                                                        }
                                                    )
                                                }
                                        ) {
                                            Text(
                                                text = homeScreenUiState.value.sortBy,
                                                style = labelSmall.notScale(),
                                                color = if (isDarkMode)
                                                    onSurfaceDark
                                                else
                                                    onSurfaceLight
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = isSortByChipExpanded,
                                        onDismissRequest = { isSortByChipExpanded = false },
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .clip(shape = RoundedCornerShape(size = 8.dp))
                                            .drawBehind {
                                                drawRect(
                                                    color = if (isDarkMode)
                                                        surfaceContainerDark
                                                    else
                                                        surfaceContainerLight
                                                )
                                            }
                                    ) {
                                        homeScreenViewModel.sortingType.forEach {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = it,
                                                        style = labelSmall.notScale(),
                                                        color = if (isDarkMode)
                                                            onSurfaceDark
                                                        else
                                                            onSurfaceLight
                                                    )
                                                },
                                                onClick = {
                                                    isSortByChipExpanded = false
                                                    homeScreenViewModel.changeSortType(sortBy = it)
                                                },
                                                contentPadding = PaddingValues(all = 0.dp),
                                                modifier = Modifier
                                                    .wrapContentSize()
                                                    .padding(
                                                        top = 4.dp,
                                                        bottom = 4.dp,
                                                        start = 8.dp,
                                                        end = 40.dp
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                            SortByChip()

                            @Composable
                            fun Notes() {
                                @Composable
                                fun Note(
                                    note: NoteUiModel,
                                    updateCompletionStatus: () -> Unit,
                                    pinOrUnpinNote: () -> Unit,
                                    deleteNote: () -> Unit
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(size = 8.dp))
                                            .fillMaxWidth()
                                            .wrapContentHeight()
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
                                                    .wrapContentSize()
                                                    .padding(all = 12.dp)
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
                                                        else
                                                            outlineVariantLight
                                                    )
                                            }
                                        }
                                        CheckBoxContainer()

                                        @Composable
                                        fun Content() {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .wrapContentHeight(),
                                                horizontalAlignment = Alignment.Start,
                                                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                                            ) {
//                                            Title
                                                Text(
                                                    text = note.title,
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
                                                    text = note.body,
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
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            space = 8.dp
                                                        ),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
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
//                                            Pin icon
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

                                                Icon(
                                                    painter = painterResource(id = R.drawable.delete_icon),
                                                    contentDescription = "delete note",
                                                    modifier = Modifier
                                                        .width(16.dp)
                                                        .height(16.dp)
                                                        .pointerInput(Unit) {
                                                            detectTapGestures(
                                                                onPress = {
                                                                    deleteNote()
                                                                }
                                                            )
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

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 16.dp)
                                        .animateContentSize(
                                            animationSpec = tween(
                                                durationMillis = 250,
                                                easing = EaseOut
                                            )
                                        ),
                                    contentPadding = PaddingValues(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                                ) {
                                    if (homeScreenUiState.value.sortBy != homeScreenViewModel.sortingType[2]) {
                                        items(count = homeScreenUiState.value.notes.size) { index ->
                                            Note(
                                                note = homeScreenUiState.value.notes[index],
                                                updateCompletionStatus = {
                                                    homeScreenViewModel.updateCompletionStatus(
                                                        id = homeScreenUiState.value.notes[index].originalIndex
                                                    )
                                                },
                                                pinOrUnpinNote = {
                                                    homeScreenViewModel.pinOrUnpin(
                                                        id = homeScreenUiState
                                                            .value
                                                            .notes[index]
                                                            .originalIndex
                                                    )
                                                },
                                                deleteNote = {
                                                    homeScreenViewModel.deleteNote(
                                                        id = homeScreenUiState
                                                            .value
                                                            .notes[index]
                                                            .originalIndex
                                                    )
                                                }
                                            )
                                        }
                                    } else {
                                        @Composable
                                        fun Folder(
                                            folderName: String,
                                            notes: List<NoteUiModel>
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight(),
                                                horizontalAlignment = Alignment.Start,
                                                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
                                            ) {
//                                                Folder name
                                                Text(
                                                    text = folderName,
                                                    style = titleLarge.notScale(),
                                                    color = if (isDarkMode)
                                                        onSurfaceDark
                                                    else
                                                        onSurfaceLight
                                                )

                                                @Composable
                                                fun Notes() {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .wrapContentHeight(),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(
                                                            space = 16.dp
                                                        )
                                                    ) {
                                                        notes.forEach {
                                                            Note(
                                                                note = it,
                                                                updateCompletionStatus = {
                                                                    homeScreenViewModel.updateCompletionStatus(
                                                                        id = it.originalIndex
                                                                    )
                                                                },
                                                                pinOrUnpinNote = {
                                                                    homeScreenViewModel.pinOrUnpin(
                                                                        id = it.originalIndex
                                                                    )
                                                                },
                                                                deleteNote = {
                                                                    homeScreenViewModel.deleteNote(
                                                                        id = it.originalIndex
                                                                    )
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                                Notes()
                                            }
                                        }
                                        item {
                                            homeScreenUiState.value.groupByFolder.forEach {
                                                Folder(
                                                    folderName = it.key,
                                                    notes = it.value
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Notes()
                        }
                    }
                    NoteListContainer()

//                Add button
                    Box(
                        modifier = Modifier
                            .padding(
                                end = 32.dp,
                                bottom = 32.dp
                            )
                            .width(width = 48.dp)
                            .height(height = 48.dp)
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .drawBehind {
                                drawRoundRect(
                                    color = if (isDarkMode)
                                        primaryContainerDark
                                    else
                                        primaryContainerLight,
                                    cornerRadius = CornerRadius(x = 8.dp.toPx())
                                )
                            }
                            .align(alignment = Alignment.BottomEnd)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        navController.navigate(
                                            route = Destination.CreateNoteScreen.route
                                        )
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_icon),
                            contentDescription = "add new note",
                            modifier = Modifier
                                .width(width = 20.dp)
                                .height(20.dp),
                            tint = if (isDarkMode)
                                onPrimaryContainerDark
                            else
                                onPrimaryContainerLight
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    @Composable
                    fun EmptyHomeScreenContent() {
                        Column(
                            modifier = Modifier.wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(space = 20.dp)
                        ) {
                            @Composable
                            fun Status() {
                                Column(
                                    modifier = Modifier.wrapContentSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(
                                            id = if (isDarkMode)
                                                R.drawable.empty_note_figure_dark
                                            else
                                                R.drawable.empty_note_figure
                                        ),
                                        contentDescription = "empty note figure",
                                        modifier = Modifier
                                            .width(width = 80.dp)
                                            .height(height = 80.dp),
                                        contentScale = ContentScale.FillBounds,
                                    )

                                    Text(
                                        text = stringResource(id = R.string.empty_status_sentence),
                                        style = bodySmall.notScale(),
                                        color = if (isDarkMode)
                                            outlineVariantDark
                                        else
                                            outlineVariantLight
                                    )
                                }
                            }
                            Status()

                            @Composable
                            fun CtaButton() {
                                var isPress: Boolean by remember {
                                    mutableStateOf(value = false)
                                }

                                val buttonElevation: Dp by animateDpAsState(
                                    targetValue = if (isPress)
                                        0.dp
                                    else
                                        1.dp,
                                    label = "button elevation interaction"
                                )

                                Box(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .clip(shape = RoundedCornerShape(size = 8.dp))
                                        .shadow(
                                            elevation = buttonElevation,
                                            shape = RoundedCornerShape(size = 8.dp)
                                        )
                                        .drawBehind {
                                            drawRoundRect(
                                                color = if (isDarkMode)
                                                    primaryContainerDark
                                                else
                                                    primaryContainerLight,
                                                cornerRadius = CornerRadius(x = 8.dp.toPx())
                                            )
                                        }
                                        .padding(
                                            top = 8.dp,
                                            bottom = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    isPress = true
                                                    tryAwaitRelease()
                                                    isPress = false
                                                    navController.navigate(route = Destination.CreateNoteScreen.route)
                                                }
                                            )
                                        }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.button_label_create),
                                        style = labelSmall.notScale(),
                                        color = if (isDarkMode)
                                            onPrimaryContainerDark
                                        else
                                            onPrimaryContainerLight
                                    )
                                }
                            }
                            CtaButton()
                        }
                    }
                    EmptyHomeScreenContent()
                }
            }
        }

        @Composable
        fun InfoScreen() {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                @Composable
                fun DarkModeControl() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 28.dp,
                                end = 28.dp
                            )
                            .wrapContentHeight()
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
                                start = 16.dp,
                                end = 16.dp,
                                top = 12.dp,
                                bottom = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Title
                        Text(
                            text = stringResource(id = R.string.control_title_dark_mode),
                            style = bodySmall.notScale(),
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight,
                        )
                        //                        Switch
                        @Composable
                        fun Switch() {

                            val xThumb: Dp by animateDpAsState(
                                targetValue = if (isDarkMode)
                                    28.dp
                                else
                                    12.dp,
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "position of thumb of switch"
                            )

                            val containerColor: Color by animateColorAsState(
                                targetValue = if (isDarkMode)
                                    primaryContainerDark
                                else
                                    Color(color = 0x291D1B20),
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "container color of switch"
                            )

                            val thumbColor: Color by animateColorAsState(
                                targetValue = if (isDarkMode)
                                    onPrimaryContainerDark.copy(alpha = 0.5f)
                                else
                                    surfaceLight.copy(alpha = 0.5f),
                                tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "thumb color of switch"
                            )

                            Box(
                                modifier = Modifier
                                    .width(width = 40.dp)
                                    .height(height = 24.dp)
                                    .clip(shape = RoundedCornerShape(size = 12.dp))
                                    .drawBehind {
                                        drawRoundRect(
                                            color = containerColor,
                                            cornerRadius = CornerRadius(x = 12.dp.toPx())
                                        )
                                        drawCircle(
                                            color = thumbColor,
                                            radius = 8.dp.toPx(),
                                            center = Offset(
                                                x = xThumb.toPx(),
                                                y = 12.dp.toPx()
                                            )
                                        )
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                saveAndSwitchUiMode()
                                            }
                                        )
                                    }
                            )
                        }
                        Switch()
                    }
                }
                DarkModeControl()

                Spacer(modifier = Modifier.height(height = 16.dp))

//                About app title
                Text(
                    text = stringResource(id = R.string.about_app_title),
                    style = titleMedium.notScale(),
                    color = if (isDarkMode)
                        onSurfaceDark
                    else
                        onSurfaceLight
                )

                Spacer(modifier = Modifier.height(height = 8.dp))

                @Composable
                fun AppInfo() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 28.dp,
                                end = 28.dp
                            ),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                    ) {
//                        Version
                        Text(
                            text = stringResource(id = R.string.about_app_version),
                            style = bodySmall.notScale(),
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )

//                        Contact
                        Text(
                            text = stringResource(id = R.string.about_app_contact),
                            style = bodySmall.notScale(),
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )

//                        Donate channel
                        Text(
                            text = stringResource(id = R.string.about_app_donate_channel),
                            style = bodySmall.notScale(),
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )
                    }
                }
                AppInfo()
            }
        }

        AnimatedContent(
            targetState = selectedMainScreenDestination,
            transitionSpec = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseIn),
                    towards = if (targetState == 0)
                        AnimatedContentTransitionScope.SlideDirection.Right
                    else
                        AnimatedContentTransitionScope.SlideDirection.Left
                ) togetherWith slideOutOfContainer(
                    animationSpec = tween(durationMillis = 300, easing = EaseOut),
                    towards = if (targetState == 0)
                        AnimatedContentTransitionScope.SlideDirection.Right
                    else
                        AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            label = "Home Screen and InfoScreen Transition"
        ) { targetState ->
            when (targetState) {
                0 -> HomeScreen()
                else -> InfoScreen()
            }
        }
    }
}