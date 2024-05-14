package app.kotlin.snapnote.ui.views

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.kotlin.snapnote.R
import app.kotlin.snapnote.ui.theme.bodyMedium
import app.kotlin.snapnote.ui.theme.headlineSmall
import app.kotlin.snapnote.ui.theme.labelLarge
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onPrimaryContainerDark
import app.kotlin.snapnote.ui.theme.onPrimaryContainerLight
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.outlineDark
import app.kotlin.snapnote.ui.theme.outlineLight
import app.kotlin.snapnote.ui.theme.primaryContainerDark
import app.kotlin.snapnote.ui.theme.primaryContainerLight
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight

@Composable
fun DraftScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 40.dp)
    ) {
        repeat(times = 4) {
            Box(
                modifier = Modifier
                    .width(width = 80.dp)
                    .height(height = 80.dp)
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .background(
                        color = Color.Red,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
            )
        }
        var showDialog: Boolean by remember {
            mutableStateOf(value = false)
        }

        Button(onClick = { showDialog = true }) {
            Text(text = "Click me")
        }
        if (showDialog)
            RenameFolderDialog2(onDismissRequest = { showDialog = false })
    }
}

@Composable
fun RenameFolderDialog2(
    isDarkMode: Boolean = false,
    onDismissRequest: () -> Unit
) {
    var newFolderName: String by remember {
        mutableStateOf(value = "")
    }
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            var isPressed: Boolean by remember {
                mutableStateOf(value = false)
            }

            val buttonElevation: Int by animateIntAsState(
                targetValue =
                if (isPressed)
                    0
                else
                    1,
                label = "button elevation interaction"
            )

            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .shadow(
                        elevation = buttonElevation.dp,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .drawBehind {
                        if (newFolderName != "") {
                            drawRoundRect(
                                color = if (isDarkMode)
                                    primaryContainerDark
                                else
                                    primaryContainerLight,
                                cornerRadius = CornerRadius(x = 8.dp.toPx())
                            )
                        } else {
                            drawRoundRect(
                                color = if (isDarkMode)
                                    Color(color = 0xfffaf1ea)
                                else
                                    Color(color = 0xffbcb5b0),
                                cornerRadius = CornerRadius(x = 8.dp.toPx())
                            )
                        }
                    }
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = if (newFolderName != "") {
                                {
                                    isPressed = true
                                    tryAwaitRelease()
                                    isPressed = false
                                    /* TODO */
                                }
                            } else {
                                {
                                    /* TODO */
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.button_label_save),
                    style = labelLarge.notScale(),
                    color = if (newFolderName != "") {
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
            val interactionSource: MutableInteractionSource = remember {
                MutableInteractionSource()
            }

            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 8.dp))
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onDismissRequest() }
                    )
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
            OutlinedTextField(
                value = newFolderName,
                onValueChange = { newFolderName = it },
                textStyle = bodyMedium.notScale(),
                placeholder = {
                    Text(
                        text = "Enter the folder name",
                        style = bodyMedium.notScale(),
                        color = if (isDarkMode)
                            outlineDark
                        else
                            outlineLight
                    )
                },
                trailingIcon = {
                    val interactionSource: MutableInteractionSource = remember {
                        MutableInteractionSource()
                    }
                    if (newFolderName != "")
                        Icon(
                            painter = painterResource(id = R.drawable.cancel_icon),
                            contentDescription = "clear text",
                            modifier = Modifier
                                .width(width = 24.dp)
                                .height(height = 24.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { newFolderName = "" }
                                )
                        )
                }
            )
        },
        shape = RoundedCornerShape(size = 16.dp)
    )
}