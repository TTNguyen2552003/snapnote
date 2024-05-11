package app.kotlin.snapnote.ui.views

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import app.kotlin.snapnote.R
import app.kotlin.snapnote.ui.theme.bodySmall
import app.kotlin.snapnote.ui.theme.labelLarge
import app.kotlin.snapnote.ui.theme.labelSmall
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onPrimaryContainerDark
import app.kotlin.snapnote.ui.theme.onPrimaryContainerLight
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.outlineDark
import app.kotlin.snapnote.ui.theme.outlineLight
import app.kotlin.snapnote.ui.theme.primaryContainerDark
import app.kotlin.snapnote.ui.theme.primaryContainerLight
import app.kotlin.snapnote.ui.theme.surfaceContainerDark
import app.kotlin.snapnote.ui.theme.surfaceContainerLight
import app.kotlin.snapnote.ui.theme.surfaceContainerLowDark
import app.kotlin.snapnote.ui.theme.surfaceContainerLowLight
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight

const val MAX_TITLE_LENGTH = 100
const val MAX_BODY_LENGTH = 1000

@Composable
fun CreateNoteScreen(isDarkMode: Boolean = false) {
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
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                @Composable
                fun DiscardButton() {
                    val interactionSource: MutableInteractionSource = remember {
                        MutableInteractionSource()
                    }

                    Box(
                        modifier = Modifier
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
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource
                            ) {
                                /* TODO */
                            }
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_label_discard),
                            style = labelSmall.notScale(),
                            color = if (isDarkMode) outlineDark else outlineLight
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
                                        /* TODO */
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
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(space = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                @Composable
                fun FolderField() {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

//                        Folder
                        PickerChip(
                            isDarkMode = isDarkMode,
                            iconRes = R.drawable.folder_icon,
                            iconContentDescription = "folder icon",
                            label = "Uncategorized"
                        ) { /* TODO */ }

//                        Create a new folder button
                        val interactionSource: MutableInteractionSource = remember {
                            MutableInteractionSource()
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.create_new_folder),
                            contentDescription = "Create a new folder button",
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    /* TODO */
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
                                    text = "title: " + "0" + "/100",
                                    style = bodySmall.notScale(),
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else
                                        onSurfaceLight
                                )

//                                Body length
                                Text(
                                    text = "body: " + "0" + "/1000",
                                    style = bodySmall.notScale(),
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else onSurfaceLight
                                )
                            }
                        }
                        LengthsOfForm()

//                        Copy button
                        val interactionSource: MutableInteractionSource = remember {
                            MutableInteractionSource()
                        }
                        val clipboardManager: ClipboardManager = LocalClipboardManager.current
                        val context: Context = LocalContext.current

                        Icon(
                            painter = painterResource(id = R.drawable.copy_icon),
                            contentDescription = "copy button",
                            modifier = Modifier
                                .width(width = 16.dp)
                                .height(height = 16.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    val annotatedString = AnnotatedString(text = "Hello world")
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
                        var title: String by remember { mutableStateOf(value = "") }
                        BasicTextField(
                            value = if (title.length <= MAX_TITLE_LENGTH)
                                title
                            else
                                title.substring(
                                    startIndex = 0,
                                    endIndex = MAX_TITLE_LENGTH + 1
                                ),
                            onValueChange = { title = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth: Float = (0.5).dp.toPx()
                                    val y: Float = size.height - strokeWidth
                                    drawLine(
                                        color = if (isDarkMode)
                                            outlineDark
                                        else
                                            outlineLight,
                                        start = Offset(
                                            x = 0.dp.toPx(),
                                            y = y
                                        ),
                                        end = Offset(
                                            x = size.width,
                                            y = y
                                        )
                                    )
                                }
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .horizontalScroll(state = rememberScrollState()),
                            textStyle = labelLarge
                                .copy(
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else
                                        onSurfaceLight
                                )
                                .notScale(),
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
                                if (title.isEmpty()) {
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

                        var body: String by remember {
                            mutableStateOf(value = "")
                        }
                        BasicTextField(
                            value = if (body.length <= MAX_BODY_LENGTH)
                                body
                            else
                                body.substring(
                                    startIndex = 0,
                                    endIndex = MAX_BODY_LENGTH + 1
                                ),
                            onValueChange = { body = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .verticalScroll(state = rememberScrollState()),
                            textStyle = bodySmall
                                .copy(
                                    color = if (isDarkMode)
                                        onSurfaceDark
                                    else onSurfaceLight
                                )
                                .notScale(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            cursorBrush = SolidColor(
                                if (isDarkMode)
                                    onPrimaryContainerDark
                                else
                                    onPrimaryContainerLight
                            ),
                            decorationBox = { innerTextField ->
                                if (body.isEmpty()) {
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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        @Composable
                        fun ReminderPicker() {
                            Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
//                                Date picker
                                PickerChip(
                                    isDarkMode = isDarkMode,
                                    iconRes = R.drawable.date_icon,
                                    iconContentDescription = "date picker",
                                    label = "Date"
                                ) {
                                    /* TODO */
                                }

//                                Time picker
                                PickerChip(
                                    isDarkMode = isDarkMode,
                                    iconRes = R.drawable.time_icon,
                                    iconContentDescription = "time picker",
                                    label = "Time"
                                ) {
                                    /* TODO */
                                }
                            }
                        }
                        ReminderPicker()

                        @Composable
                        fun ReminderSwitch() {
                            val interactionSource: MutableInteractionSource = remember {
                                MutableInteractionSource()
                            }

                            var checked: Boolean by remember {
                                mutableStateOf(value = false)
                            }

                            val containerColor: Color by animateColorAsState(
                                targetValue = if (checked) {
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
                                targetValue = if (checked) {
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
                                targetValue = if (checked)
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
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = {
                                            checked = !checked
                                            /* TODO */
                                        }
                                    )
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
    val interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    }
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
            .padding(
                top = 4.dp,
                bottom = 4.dp,
                start = 8.dp,
                end = 8.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
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