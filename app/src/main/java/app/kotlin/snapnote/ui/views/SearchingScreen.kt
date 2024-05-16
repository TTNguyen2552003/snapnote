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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.kotlin.snapnote.R
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

@Composable
fun SearchingScreen(isDarkMode: Boolean = false) {
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
                    var keyword: String by remember {
                        mutableStateOf(value = "")
                    }

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

                                BasicTextField(
                                    value = keyword,
                                    onValueChange = { keyword = it },
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .weight(weight = 1f),
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
                                        if (keyword.isEmpty()) {
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
                        if (keyword.isNotEmpty())
                            Icon(
                                painter = painterResource(id = R.drawable.cancel_icon),
                                contentDescription = "clear search",
                                modifier = Modifier
                                    .width(width = 24.dp)
                                    .height(height = 24.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                keyword = ""
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
                                /* TODO */
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
            val sampleNote = NoteShown()

            var sampleIsDone: Boolean by remember {
                mutableStateOf(value = false)
            }

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
                fun Note() {
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .drawBehind {
                                drawRoundRect(
                                    color = if (sampleIsDone) {
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
                                                sampleIsDone = !sampleIsDone
                                                /* TODO */
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (!sampleIsDone)
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
                                    text = sampleNote.title,
                                    style = titleSmall.notScale(),
                                    color = if (!sampleIsDone) {
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
                                val targetWord = "sample"
                                val startIndex: Int = sampleNote.body.indexOf(string = targetWord)
                                val endIndex: Int = startIndex + targetWord.length
                                val highlights: AnnotatedString = buildAnnotatedString {
                                    append(
                                        text = sampleNote
                                            .body
                                            .substring(
                                                startIndex = 0,
                                                endIndex = startIndex
                                            )
                                    )
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color(color = 0xFFDC3545),
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(
                                            text = sampleNote
                                                .body
                                                .substring(
                                                    startIndex = startIndex,
                                                    endIndex = endIndex + 1
                                                )
                                        )
                                    }
                                    append(
                                        text = sampleNote
                                            .body
                                            .substring(
                                                startIndex = endIndex + 1,
                                                endIndex = sampleNote.body.length
                                            )
                                    )
                                }
                                Text(
                                    text = highlights,
                                    style = bodySmall.notScale(),
                                    color = if (!sampleIsDone) {
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
                                        horizontalArrangement = Arrangement
                                            .spacedBy(space = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.reminder_icon),
                                            contentDescription = "reminder",
                                            modifier = Modifier
                                                .width(width = 16.dp)
                                                .height(height = 16.dp),
                                            tint = if (!sampleIsDone) {
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
                                                    text = sampleNote.date.toString(),
                                                    style = labelSmall.notScale(),
                                                    color = if (!sampleIsDone) {
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
                                                    text = sampleNote.time.toString(),
                                                    style = labelSmall.notScale(),
                                                    color = if (!sampleIsDone) {
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
                                var isPinned: Boolean by remember {
                                    mutableStateOf(value = false)
                                }

                                Icon(
                                    painter = painterResource(
                                        id = if (isPinned)
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
                                                    isPinned = !isPinned
                                                    /* TODO */
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
                                        .height(16.dp),
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

                items(count = 2) {
                    Note()
                }
            }
        }
        Results()
    }
}