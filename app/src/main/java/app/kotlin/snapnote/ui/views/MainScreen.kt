package app.kotlin.snapnote.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.kotlin.snapnote.R
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
import app.kotlin.snapnote.ui.theme.surfaceLight
import app.kotlin.snapnote.ui.theme.titleMedium
import app.kotlin.snapnote.ui.theme.titleSmall
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt


data class NoteShown(
    var isDone: Boolean = false,
    var title: String = "Unknown titled",
    var body: String = "This is a sample note",
    var date: LocalDate = LocalDate.now(),
    private var originalTime: LocalTime = LocalTime.now().plusMinutes(1),
) {
    val time: LocalTime
        get() = originalTime
            .withSecond(
                (originalTime.second + originalTime.nano / 1_000_000_000.0).roundToInt()
            )
            .withNano(0)
}

@Preview
@Composable
fun MainScreen(isDarkMode: Boolean = false) {

    var selected: Int by remember {
        mutableIntStateOf(value = 0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Icon(
                    painter = painterResource(
                        id = if (selected == 0)
                            R.drawable.home_filled_icon
                        else
                            R.drawable.home_icon
                    ),
                    contentDescription = "home tab",
                    tint = if (selected == 0) {
                        if (isDarkMode)
                            primaryContainerDark
                        else primaryContainerLight
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
                                    selected = 0
                                }
                            )
                        }
                )

                Spacer(modifier = Modifier.width(width = 80.dp))

                Icon(
                    painter = painterResource(
                        id = if (selected == 1)
                            R.drawable.info_filled_icon
                        else
                            R.drawable.info_icon
                    ),
                    contentDescription = "info tab",
                    tint = if (selected == 1) {
                        if (isDarkMode)
                            primaryContainerDark
                        else
                            primaryContainerLight
                    } else {
                        if (isDarkMode)
                            outlineDark
                        else
                            outlineLight
                    },
                    modifier = Modifier
                        .width(width = 40.dp)
                        .height(height = 40.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    selected = 1
                                }
                            )
                        }
                )
            }
        }
        TopBarNavigation()

        Spacer(modifier = Modifier.height(16.dp))

        @Composable
        fun HomeScreen() {
            Box(modifier = Modifier.fillMaxSize()) {

                val sortBy: String by remember {
                    mutableStateOf(value = "none")
                }

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
                                    .height(height = 40.dp)
                                    .fillMaxWidth()
                                    .padding(start = 16.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                /* TODO */
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
                        fun SortBy() {
                            if (sortBy == "none") {
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
                                        .align(alignment = Alignment.End)
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
                                        .align(alignment = Alignment.End)
                                ) {
                                    Text(
                                        text = sortBy,
                                        style = labelSmall.notScale(),
                                        color = if (isDarkMode)
                                            onSurfaceDark
                                        else
                                            onSurfaceLight
                                    )
                                }
                            }
                        }
                        SortBy()

                        @Composable
                        fun Notes() {
                            val sampleNote = NoteShown()

                            var sampleIsDone: Boolean by remember {
                                mutableStateOf(value = false)
                            }

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
                                            Text(
                                                text = sampleNote.body,
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

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(count = 12) {
                                    Note()
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
                        .align(alignment = Alignment.BottomEnd),
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
        }

        @Composable
        fun InfoScreen() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                @Composable
                fun DarkModeControl() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            var isSwitchOn: Boolean by remember {
                                mutableStateOf(value = false)
                            }

                            val xThumb: Dp by animateDpAsState(
                                targetValue = if (isSwitchOn)
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
                                targetValue = if (isSwitchOn)
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
                                targetValue = if (isSwitchOn)
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
                                                isSwitchOn = !isSwitchOn
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
                        modifier = Modifier.fillMaxWidth(),
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

        if (selected == 0)
            HomeScreen()
        else if (selected == 1)
            InfoScreen()
    }
}

