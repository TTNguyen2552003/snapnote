package app.kotlin.snapnote.ui.views

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.kotlin.snapnote.MAX_BODY_LENGTH
import app.kotlin.snapnote.MAX_FOLDER_NAME_LENGTH
import app.kotlin.snapnote.MAX_TITLE_LENGTH
import app.kotlin.snapnote.R
import app.kotlin.snapnote.ui.theme.bodyLarge
import app.kotlin.snapnote.ui.theme.bodyMedium
import app.kotlin.snapnote.ui.theme.bodySmall
import app.kotlin.snapnote.ui.theme.headlineSmall
import app.kotlin.snapnote.ui.theme.labelLarge
import app.kotlin.snapnote.ui.theme.labelSmall
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onPrimaryContainerDark
import app.kotlin.snapnote.ui.theme.onPrimaryContainerLight
import app.kotlin.snapnote.ui.theme.onPrimaryDark
import app.kotlin.snapnote.ui.theme.onPrimaryLight
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.onSurfaceVariantDark
import app.kotlin.snapnote.ui.theme.onSurfaceVariantLight
import app.kotlin.snapnote.ui.theme.onTertiaryContainerDark
import app.kotlin.snapnote.ui.theme.onTertiaryContainerLight
import app.kotlin.snapnote.ui.theme.outlineDark
import app.kotlin.snapnote.ui.theme.outlineLight
import app.kotlin.snapnote.ui.theme.primaryContainerDark
import app.kotlin.snapnote.ui.theme.primaryContainerLight
import app.kotlin.snapnote.ui.theme.primaryDark
import app.kotlin.snapnote.ui.theme.primaryLight
import app.kotlin.snapnote.ui.theme.robotoFamily
import app.kotlin.snapnote.ui.theme.surfaceContainerDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighDark
import app.kotlin.snapnote.ui.theme.surfaceContainerHighLight
import app.kotlin.snapnote.ui.theme.surfaceContainerLight
import app.kotlin.snapnote.ui.theme.surfaceContainerLowDark
import app.kotlin.snapnote.ui.theme.surfaceContainerLowLight
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight
import app.kotlin.snapnote.ui.theme.surfaceVariantDark
import app.kotlin.snapnote.ui.theme.surfaceVariantLight
import app.kotlin.snapnote.ui.theme.tertiaryContainerDark
import app.kotlin.snapnote.ui.theme.tertiaryContainerLight
import app.kotlin.snapnote.ui.viewmodels.CreateNoteScreenUiState
import app.kotlin.snapnote.ui.viewmodels.CreateNoteScreenViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun CreateNoteScreen(
    isDarkMode: Boolean = false,
    navController: NavController,
    createNoteScreenViewModel: CreateNoteScreenViewModel = viewModel(factory = CreateNoteScreenViewModel.factory)
) {
    val createNoteScreenUiState: State<CreateNoteScreenUiState> =
        createNoteScreenViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isDarkMode)
                    surfaceDark
                else
                    surfaceLight
            )
            .windowInsetsPadding(insets = WindowInsets.statusBars)
    ) {
        Spacer(modifier = Modifier.height(height = 8.dp))

        @Composable
        fun ActionPanel() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                @Composable
                fun DiscardButton() {
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = if (isDarkMode)
                                        surfaceContainerLowDark
                                    else
                                        surfaceContainerLowLight,
                                    cornerRadius = CornerRadius(x = 8.dp.toPx())
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
                                        navController.popBackStack()
                                    }
                                )
                            }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_label_discard),
                            style = labelSmall.notScale(),
                            color = if (isDarkMode)
                                outlineDark
                            else
                                outlineLight
                        )
                    }
                }
                DiscardButton()

                @Composable
                fun DoneButton() {
                    var isPress: Boolean by remember {
                        mutableStateOf(value = false)
                    }

                    val buttonElevation: Int by animateIntAsState(
                        targetValue = if (isPress)
                            0
                        else
                            1,
                        label = "button elevation interaction"
                    )

                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = buttonElevation.dp,
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .clip(shape = RoundedCornerShape(size = 8.dp))
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
                                top = 4.dp,
                                bottom = 4.dp,
                                start = 8.dp,
                                end = 8.dp
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isPress = true
                                        tryAwaitRelease()
                                        isPress = false
                                        createNoteScreenViewModel.saveNote()
                                        navController.popBackStack()
                                    }
                                )
                            }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_label_done),
                            style = labelSmall.notScale(),
                            color = if (isDarkMode)
                                onPrimaryContainerDark
                            else
                                onPrimaryContainerLight
                        )
                    }
                }
                DoneButton()
            }
        }
        ActionPanel()

        Spacer(modifier = Modifier.height(height = 28.dp))

        @Composable
        fun Components() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                var showDialog: Boolean by remember {
                    mutableStateOf(value = false)
                }

                if (showDialog)
                    RenameFolderDialog(
                        isDarkMode = isDarkMode,
                        onDismissRequest = { showDialog = false },
                        onSaveCurrentFolderName = {
                            createNoteScreenViewModel.updateCurrentNewFolderName(newFolderName = it)
                        }
                    )

                @Composable
                fun FolderField() {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        @Composable
                        fun Folder() {
                            var isMenuFolderExpanded: Boolean by remember {
                                mutableStateOf(value = false)
                            }

                            PickerChip(
                                isDarkMode = isDarkMode,
                                iconRes = R.drawable.folder_icon,
                                iconContentDescription = "folder icon",
                                label = createNoteScreenUiState.value.currentFolderName
                            ) {
                                if (createNoteScreenViewModel.folders.isNotEmpty())
                                    isMenuFolderExpanded = true
                            }

                            DropdownMenu(
                                expanded = isMenuFolderExpanded,
                                onDismissRequest = { isMenuFolderExpanded = false },
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
                                createNoteScreenViewModel.folders.forEach {
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
                                            isMenuFolderExpanded = false
                                            createNoteScreenViewModel.updateCurrentNewFolderName(
                                                newFolderName = it
                                            )
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
                        Folder()


//                        Create a new folder button
                        Icon(
                            painter = painterResource(id = R.drawable.create_new_folder),
                            contentDescription = "Create a new folder button",
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            showDialog = true
                                            /* TODO */
                                        }
                                    )
                                },
                            tint = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )
                    }
                }
                FolderField()

                @Composable
                fun StatisticsAndCopyButton() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                start = 4.dp,
                                end = 4.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        @Composable
                        fun LengthsOfForm() {
                            Row(horizontalArrangement = Arrangement.spacedBy(space = 40.dp)) {
//                                Title length
                                Text(
                                    text = "title: "
                                            + "${createNoteScreenUiState.value.title.length}"
                                            + "/$MAX_TITLE_LENGTH",
                                    style = bodySmall.notScale(),
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else
                                        onSurfaceLight
                                )

//                                Body length
                                Text(
                                    text = "body: "
                                            + "${createNoteScreenUiState.value.body.length}"
                                            + "/$MAX_BODY_LENGTH",
                                    style = bodySmall.notScale(),
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else onSurfaceLight
                                )
                            }
                        }
                        LengthsOfForm()

//                        Copy button
                        val clipboardManager: ClipboardManager = LocalClipboardManager.current
                        val context: Context = LocalContext.current
                        Icon(
                            painter = painterResource(id = R.drawable.copy_icon),
                            contentDescription = "copy button",
                            modifier = Modifier
                                .width(width = 16.dp)
                                .height(height = 16.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            val annotatedString =
                                                AnnotatedString(
                                                    text = createNoteScreenUiState.value.title
                                                            + " "
                                                            + createNoteScreenUiState.value.body
                                                )
                                            clipboardManager.setText(annotatedString)
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Text copied to clipboard",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    )
                                },
                            tint = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )
                    }
                }
                StatisticsAndCopyButton()

                @Composable
                fun Form() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 320.dp)
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = if (isDarkMode)
                                        surfaceContainerLowDark
                                    else
                                        surfaceContainerLowLight
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .drawBehind {
                                    drawLine(
                                        color = if (isDarkMode)
                                            outlineDark
                                        else
                                            outlineLight,
                                        start = Offset(
                                            x = 0.dp.toPx(),
                                            y = size.height - 0.5.dp.toPx(),
                                        ),
                                        end = Offset(
                                            x = size.width,
                                            y = size.height - 0.5.dp.toPx()
                                        ),
                                        strokeWidth = 0.5.dp.toPx()
                                    )
                                }
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BasicTextField(
                                value = createNoteScreenUiState.value.title,
                                onValueChange = {
                                    createNoteScreenViewModel.updateTitle(newTitle = it)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight(),
                                textStyle = labelLarge
                                    .notScale()
                                    .copy(
                                        color = if (isDarkMode)
                                            onSurfaceDark
                                        else
                                            onSurfaceLight
                                    ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                singleLine = true,
                                cursorBrush = SolidColor(
                                    if (isDarkMode)
                                        onPrimaryContainerDark
                                    else
                                        onPrimaryContainerLight
                                ),
                                decorationBox = { innerTextField ->
                                    if (createNoteScreenUiState.value.title.isEmpty()) {
                                        Text(
                                            text = stringResource(id = R.string.place_holder_note_title),
                                            style = labelLarge.notScale(),
                                            color = if (isDarkMode)
                                                outlineDark
                                            else
                                                outlineLight
                                        )
                                    }
                                    innerTextField()
                                }
                            )

//                            Trailing icon
                            if (createNoteScreenUiState.value.title.isNotEmpty())
                                Icon(
                                    painter = painterResource(id = R.drawable.cancel_icon),
                                    contentDescription = "clear text",
                                    modifier = Modifier
                                        .width(width = 20.dp)
                                        .height(height = 20.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    createNoteScreenViewModel.updateTitle(newTitle = "")
                                                }
                                            )
                                        },
                                    tint = if (isDarkMode)
                                        outlineDark
                                    else
                                        outlineLight
                                )
                        }

                        BasicTextField(
                            value = createNoteScreenUiState.value.body,
                            onValueChange = {
                                createNoteScreenViewModel.updateBody(newBody = it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .verticalScroll(state = rememberScrollState()),
                            textStyle = bodySmall
                                .notScale()
                                .copy(
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else
                                        onSurfaceLight
                                ),
                            keyboardOptions = KeyboardOptions
                                .Default
                                .copy(imeAction = ImeAction.Done),
                            cursorBrush = SolidColor(
                                if (isDarkMode)
                                    onPrimaryContainerDark
                                else
                                    onPrimaryContainerLight
                            ),
                            decorationBox = { innerTextField ->
                                if (createNoteScreenUiState.value.body.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.place_holder_note_body),
                                        style = bodySmall.notScale(),
                                        color = if (isDarkMode)
                                            outlineDark
                                        else
                                            outlineLight
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
                Form()

                @Composable
                fun Reminder() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        @Composable
                        fun ReminderPicker() {
                            Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {

                                @Composable
                                fun DatePickerChip() {
                                    var isDatePickerModelOpen: Boolean by remember {
                                        mutableStateOf(value = false)
                                    }

                                    PickerChip(
                                        isDarkMode = isDarkMode,
                                        iconRes = R.drawable.date_icon,
                                        iconContentDescription = "date picker",
                                        label = createNoteScreenUiState.value.date,
                                        onClick = { isDatePickerModelOpen = true }
                                    )

                                    val onConfirm: (Long) -> Unit = { dateInMilSec ->
                                        val instant: Instant = Instant.ofEpochMilli(dateInMilSec)
                                        val zoneId: ZoneId = ZoneId.systemDefault()
                                        val localDate: LocalDate =
                                            instant.atZone(zoneId).toLocalDate()

                                        val dateFormatter: DateTimeFormatter = DateTimeFormatter
                                            .ofLocalizedDate(FormatStyle.SHORT)
                                            .withLocale(Locale.getDefault())

                                        val newDate: String =
                                            dateFormatter.format(localDate)
                                        createNoteScreenViewModel.updateDate(newDate = newDate)
                                    }

                                    if (isDatePickerModelOpen) {
                                        DatePickerModel(
                                            isDarkMode = isDarkMode,
                                            onDismissRequest = { isDatePickerModelOpen = false },
                                            onConfirm = onConfirm
                                        )
                                    }
                                }
                                DatePickerChip()

                                @Composable
                                fun TimePickerChip() {
                                    var isTimePickerModelOpen: Boolean by remember {
                                        mutableStateOf(value = false)
                                    }

                                    PickerChip(
                                        isDarkMode = isDarkMode,
                                        iconRes = R.drawable.time_icon,
                                        iconContentDescription = "time picker",
                                        label = createNoteScreenUiState.value.time,
                                        onClick = { isTimePickerModelOpen = true }
                                    )

                                    val onConfirm: (LocalTime) -> Unit = { localTime ->
                                        val dateFormatter: DateTimeFormatter = DateTimeFormatter
                                            .ofLocalizedDate(FormatStyle.SHORT)
                                            .withLocale(Locale.getDefault())

                                        val timeFormatter: DateTimeFormatter = DateTimeFormatter
                                            .ofLocalizedTime(FormatStyle.SHORT)
                                            .withLocale(Locale.getDefault())

                                        if (createNoteScreenUiState.value.date != "Date") {
                                            val parsedDate: LocalDate = LocalDate.parse(
                                                createNoteScreenUiState.value.date,
                                                dateFormatter
                                            )

                                            val currentDate: LocalDate = LocalDate.now()

                                            val currentTime: LocalTime = LocalTime.now()
                                            if (parsedDate.isEqual(currentDate) && !localTime.isAfter(
                                                    currentTime
                                                )
                                            ) {
                                                createNoteScreenViewModel.updateTime(
                                                    newTime = timeFormatter.format(
                                                        LocalTime.now().plusMinutes(1)
                                                    )
                                                )
                                            } else {
                                                createNoteScreenViewModel.updateTime(
                                                    newTime = timeFormatter.format(
                                                        localTime
                                                    )
                                                )
                                            }
                                        } else {
                                            createNoteScreenViewModel.updateTime(
                                                newTime = timeFormatter.format(localTime)
                                            )
                                        }
                                    }

                                    if (isTimePickerModelOpen) {
                                        TimePickerModel(
                                            isDarkMode = isDarkMode,
                                            onDismissRequest = { isTimePickerModelOpen = false },
                                            onConfirm = onConfirm
                                        )
                                    }
                                }
                                TimePickerChip()
                            }
                        }
                        ReminderPicker()

                        @Composable
                        fun ReminderSwitch() {
                            val containerColor: Color by animateColorAsState(
                                targetValue = if (createNoteScreenUiState.value.isReminderSet) {
                                    if (isDarkMode)
                                        primaryContainerDark
                                    else
                                        primaryContainerLight
                                } else {
                                    if (isDarkMode)
                                        Color(color = 0x29E6E0E9)
                                    else
                                        Color(color = 0x291D1B20)
                                },
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "switch container color"
                            )

                            val thumbColor: Color by animateColorAsState(
                                targetValue = if (createNoteScreenUiState.value.isReminderSet) {
                                    if (isDarkMode)
                                        onPrimaryContainerDark.copy(alpha = 0.5f)
                                    else
                                        onPrimaryContainerLight.copy(alpha = 0.5f)
                                } else {
                                    if (isDarkMode)
                                        surfaceDark.copy(alpha = 0.5f)
                                    else
                                        surfaceLight.copy(alpha = 0.5f)
                                },
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "switch thumb color"
                            )

                            val xThumb: Int by animateIntAsState(
                                targetValue = if (createNoteScreenUiState.value.isReminderSet)
                                    28
                                else
                                    12,
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = EaseOut
                                ),
                                label = "x switch thumb position"
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
                                                xThumb.dp.toPx(),
                                                12.dp.toPx()
                                            )
                                        )
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                createNoteScreenViewModel.pressOnSwitch()
                                                /* TODO */
                                            }
                                        )
                                    }
                            )
                        }
                        ReminderSwitch()
                    }
                }
                Reminder()
            }
        }
        Components()
    }
}

@Composable
fun PickerChip(
    isDarkMode: Boolean,
    iconRes: Int,
    iconContentDescription: String,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
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
                top = 4.dp,
                bottom = 4.dp,
                start = 8.dp,
                end = 8.dp
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClick()
                        /* TODO */
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = iconContentDescription,
            modifier = Modifier
                .width(width = 16.dp)
                .height(height = 16.dp),
            tint = if (isDarkMode)
                onSurfaceDark
            else
                onSurfaceLight
        )

        Text(
            text = label,
            style = labelSmall.notScale(),
            color = if (isDarkMode)
                onSurfaceDark
            else
                onSurfaceLight
        )
    }
}

@Composable
fun RenameFolderDialog(
    isDarkMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onSaveCurrentFolderName: (String) -> Unit
) {
    var newFolderName: String by remember {
        mutableStateOf(value = "Uncategorized")
    }

    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            var isPressed: Boolean by remember {
                mutableStateOf(value = false)
            }

            val buttonElevation: Int by animateIntAsState(
                targetValue = if (isPressed)
                    0
                else
                    1,
                label = "button elevation interaction"
            )

            Box(
                modifier = Modifier
                    .shadow(
                        elevation = buttonElevation.dp,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = if (newFolderName.isNotEmpty()) {
                                if (isDarkMode)
                                    primaryContainerDark
                                else
                                    primaryContainerLight
                            } else {
                                if (isDarkMode)
                                    Color(color = 0xfffaf1ea)
                                else
                                    Color(color = 0xffbcb5b0)
                            },
                            cornerRadius = CornerRadius(x = 8.dp.toPx())
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
                            onPress = if (newFolderName.isNotEmpty()) {
                                {
                                    isPressed = true
                                    tryAwaitRelease()
                                    isPressed = false
                                    onSaveCurrentFolderName(newFolderName)
                                    onDismissRequest()
                                }
                            } else {
                                {
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.button_label_save),
                    style = labelLarge.notScale(),
                    color = if (newFolderName.isNotEmpty()) {
                        if (isDarkMode)
                            onPrimaryContainerDark
                        else
                            onPrimaryContainerLight
                    } else {
                        if (isDarkMode)
                            surfaceDark
                        else
                            surfaceLight
                    }
                )
            }
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onDismissRequest()
                            }
                        )
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.button_label_cancel),
                    style = labelLarge.notScale(),
                    color = if (isDarkMode)
                        outlineDark
                    else
                        outlineLight
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.dialog_title_create_a_new_folder),
                style = headlineSmall.notScale(),
                color = if (isDarkMode)
                    onSurfaceDark
                else
                    onSurfaceLight
            )
        },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
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
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = newFolderName,
                    onValueChange = {
                        newFolderName = if (it.length > MAX_FOLDER_NAME_LENGTH)
                            it.substring(
                                startIndex = 0,
                                endIndex = MAX_FOLDER_NAME_LENGTH
                            )
                        else it
                    },
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                    textStyle = bodyMedium
                        .notScale()
                        .copy(
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        ),
                    keyboardOptions = KeyboardOptions
                        .Default
                        .copy(imeAction = ImeAction.Done),
                    singleLine = true,
                    cursorBrush = SolidColor(
                        value = if (isDarkMode)
                            outlineDark
                        else
                            outlineLight
                    ),
                    decorationBox = { innerTextField ->
                        if (newFolderName.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.place_holder_create_a_new_folder),
                                style = bodyLarge.notScale(),
                                color = if (isDarkMode)
                                    outlineDark
                                else
                                    outlineLight
                            )
                        }
                        innerTextField()
                    }
                )

//                Trailing icon
                if (newFolderName.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel_icon),
                        contentDescription = "clear text",
                        modifier = Modifier
                            .width(width = 24.dp)
                            .height(height = 24.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        newFolderName = ""
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
        },
        shape = RoundedCornerShape(size = 16.dp),
        containerColor = if (isDarkMode)
            surfaceDark
        else
            surfaceLight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModel(
    isDarkMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val datePickerState: DatePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Text(
                text = stringResource(id = R.string.button_label_ok),
                style = labelLarge.notScale(),
                color = if (
                    compareSelectedDateWithCurrentDate(
                        selectedDateMillis = datePickerState.selectedDateMillis ?: 0L
                    ) >= 0
                ) {
                    if (isDarkMode)
                        primaryDark
                    else
                        primaryLight
                } else {
                    if (isDarkMode)
                        onSurfaceDark.copy(alpha = 0.38f)
                    else
                        onSurfaceLight.copy(alpha = 0.38f)
                },
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 24.dp
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                if (
                                    compareSelectedDateWithCurrentDate(
                                        selectedDateMillis = datePickerState.selectedDateMillis
                                            ?: 0L
                                    ) >= 0
                                ) {
                                    onConfirm(datePickerState.selectedDateMillis ?: 0L)
                                    onDismissRequest()
                                } else {
                                }
                            }
                        )
                    }
            )
        },
        dismissButton = {
            Text(
                text = stringResource(id = R.string.button_label_cancel),
                style = labelLarge.notScale(),
                color = if (isDarkMode)
                    primaryDark
                else
                    primaryLight,
                modifier = Modifier
                    .padding(all = 12.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onDismissRequest()
                            }
                        )
                    }
            )
        },
        content = {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = if (isDarkMode)
                        surfaceDark
                    else
                        surfaceLight,
                    titleContentColor = if (isDarkMode)
                        onSurfaceVariantDark
                    else
                        onSurfaceVariantLight,
                    headlineContentColor = if (isDarkMode)
                        onSurfaceVariantDark
                    else
                        onSurfaceVariantLight,
                    weekdayContentColor = if (isDarkMode)
                        onSurfaceDark
                    else
                        onSurfaceLight,
                    subheadContentColor = if (isDarkMode)
                        onSurfaceVariantDark
                    else
                        onSurfaceVariantLight,
                    yearContentColor = if (isDarkMode)
                        onSurfaceVariantDark
                    else
                        onSurfaceVariantLight,
                    currentYearContentColor = if (isDarkMode)
                        primaryDark
                    else
                        primaryLight,
                    selectedYearContentColor = if (isDarkMode)
                        onPrimaryDark
                    else
                        onPrimaryLight,
                    selectedYearContainerColor = if (isDarkMode)
                        primaryDark
                    else
                        primaryLight,
                    dayContentColor = if (isDarkMode)
                        onSurfaceDark
                    else
                        onSurfaceLight,
                    selectedDayContentColor = if (isDarkMode)
                        onPrimaryDark
                    else
                        onPrimaryLight,
                    selectedDayContainerColor = if (isDarkMode)
                        primaryDark
                    else
                        primaryLight,
                    todayContentColor = if (isDarkMode)
                        primaryDark
                    else
                        primaryLight,
                    todayDateBorderColor = if (isDarkMode)
                        primaryDark
                    else
                        primaryLight
                )
            )
        },
        modifier = Modifier
            .background(color = Color.Transparent),
        colors = DatePickerDefaults.colors(
            containerColor = if (isDarkMode)
                surfaceDark
            else
                surfaceLight,
            titleContentColor = if (isDarkMode)
                onSurfaceVariantDark
            else
                onSurfaceVariantLight,
            headlineContentColor = if (isDarkMode)
                onSurfaceVariantDark
            else
                onSurfaceVariantLight,
            weekdayContentColor = if (isDarkMode)
                onSurfaceDark
            else
                onSurfaceLight,
            subheadContentColor = if (isDarkMode)
                onSurfaceVariantDark
            else
                onSurfaceVariantLight,
            yearContentColor = if (isDarkMode)
                onSurfaceVariantDark
            else
                onSurfaceVariantLight,
            currentYearContentColor = if (isDarkMode)
                primaryDark
            else
                primaryLight,
            selectedYearContentColor = if (isDarkMode)
                onPrimaryDark
            else
                onPrimaryLight,
            selectedYearContainerColor = if (isDarkMode)
                primaryDark
            else
                primaryLight,
            dayContentColor = if (isDarkMode)
                onSurfaceDark
            else
                onSurfaceLight,
            selectedDayContentColor = if (isDarkMode)
                onPrimaryDark
            else
                onPrimaryLight,
            selectedDayContainerColor = if (isDarkMode)
                primaryDark
            else
                primaryLight,
            todayContentColor = if (isDarkMode)
                primaryDark
            else
                primaryLight,
            todayDateBorderColor = if (isDarkMode)
                primaryDark
            else
                primaryLight
        )
    )
}

fun compareSelectedDateWithCurrentDate(selectedDateMillis: Long): Int {
    val selectedDate: LocalDate = Instant
        .ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val currentDate: LocalDate = LocalDate.now()
    return selectedDate.compareTo(currentDate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModel(
    isDarkMode: Boolean = false,
    onDismissRequest: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    val timePickerState: TimePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        modifier = Modifier.wrapContentSize(),
        content = {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(shape = RoundedCornerShape(size = 28.dp))
                    .drawBehind {
                        drawRoundRect(
                            color = if (isDarkMode)
                                surfaceContainerHighDark
                            else
                                surfaceContainerHighLight,
                            cornerRadius = CornerRadius(x = 28.dp.toPx())
                        )
                    },
                verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            top = 24.dp,
                            start = 24.dp,
                            end = 24.dp
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.time_picker_title),
                        style = TextStyle(
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            letterSpacing = (0.5).sp
                        ).notScale(),
                        color = if (isDarkMode)
                            onSurfaceVariantDark
                        else
                            onSurfaceVariantLight
                    )
                }

                TimePicker(
                    state = timePickerState,
                    layoutType = TimePickerLayoutType.Vertical,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = if (isDarkMode)
                            surfaceVariantDark
                        else
                            surfaceContainerLight,
                        clockDialSelectedContentColor = if (isDarkMode)
                            onPrimaryDark
                        else
                            onPrimaryLight,
                        clockDialUnselectedContentColor = if (isDarkMode)
                            onSurfaceDark
                        else
                            onSurfaceLight,
                        selectorColor = if (isDarkMode)
                            primaryDark
                        else
                            primaryLight,
                        containerColor = if (isDarkMode)
                            surfaceDark
                        else
                            surfaceLight,
                        periodSelectorSelectedContentColor = if (isDarkMode)
                            onTertiaryContainerDark
                        else
                            onTertiaryContainerLight,
                        periodSelectorBorderColor = if (isDarkMode)
                            outlineDark
                        else
                            outlineLight,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContainerColor = if (isDarkMode)
                            tertiaryContainerDark
                        else
                            tertiaryContainerLight,
                        periodSelectorUnselectedContentColor = if (isDarkMode)
                            onSurfaceDark
                        else
                            onSurfaceLight,
                        timeSelectorSelectedContainerColor = if (isDarkMode)
                            primaryContainerDark
                        else
                            primaryContainerLight,
                        timeSelectorUnselectedContentColor = if (isDarkMode)
                            onSurfaceDark
                        else
                            onSurfaceLight,
                        timeSelectorSelectedContentColor = if (isDarkMode)
                            onPrimaryContainerDark
                        else
                            onPrimaryContainerLight,
                        timeSelectorUnselectedContainerColor = if (isDarkMode)
                            surfaceVariantDark
                        else
                            surfaceVariantLight
                    )
                )

                @Composable
                fun ActionContainer() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(
                                bottom = 20.dp,
                                start = 12.dp,
                                end = 24.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        @Composable
                        fun Actions() {
                            Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
//                                Dismiss button
                                Text(
                                    text = stringResource(id = R.string.button_label_cancel),
                                    style = TextStyle(
                                        fontFamily = robotoFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        letterSpacing = (0.1).sp,
                                    ).notScale(),
                                    color = if (isDarkMode)
                                        primaryDark
                                    else
                                        primaryLight,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(all = 12.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    onDismissRequest()
                                                }
                                            )
                                        }
                                )

//                                Confirm button
                                Text(
                                    text = stringResource(id = R.string.button_label_ok),
                                    style = TextStyle(
                                        fontFamily = robotoFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        letterSpacing = (0.1).sp,
                                    ).notScale(),
                                    color = if (isDarkMode)
                                        primaryDark
                                    else
                                        primaryLight,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(all = 12.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = {
                                                    onDismissRequest()
                                                    onConfirm(
                                                        LocalTime.of(
                                                            timePickerState.hour,
                                                            timePickerState.minute
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                )
                            }
                        }
                        Actions()
                    }
                }
                ActionContainer()
            }
        }
    )
}