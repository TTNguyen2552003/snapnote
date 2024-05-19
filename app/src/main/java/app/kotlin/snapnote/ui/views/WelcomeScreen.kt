package app.kotlin.snapnote.ui.views

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.kotlin.snapnote.R
import app.kotlin.snapnote.ui.theme.bodySmall
import app.kotlin.snapnote.ui.theme.notScale
import app.kotlin.snapnote.ui.theme.onPrimaryContainerDark
import app.kotlin.snapnote.ui.theme.onPrimaryContainerLight
import app.kotlin.snapnote.ui.theme.onSurfaceDark
import app.kotlin.snapnote.ui.theme.onSurfaceLight
import app.kotlin.snapnote.ui.theme.primaryContainerDark
import app.kotlin.snapnote.ui.theme.primaryContainerLight
import app.kotlin.snapnote.ui.theme.robotoFamily
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight

@Composable
fun WelcomeScreen(
    isDarkMode: Boolean = false,
    navController: NavController
) {
    Box(
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
        Spacer(modifier = Modifier.height(8.dp))

//        App name
        Text(
            text = stringResource(id = R.string.app_name).uppercase(),
            style = TextStyle(
                fontFamily = robotoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp
            ).notScale(),
            color = if (isDarkMode)
                onSurfaceDark
            else
                onSurfaceLight,
            modifier = Modifier.align(alignment = Alignment.TopCenter)
        )

        @Composable
        fun Content() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    )
                    .align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sun_flower),
                    contentDescription = "sun flower",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )

                @Composable
                fun Main() {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.graceful_sentence),
                            style = bodySmall.notScale(),
                            color = if (isDarkMode) onSurfaceDark else onSurfaceLight,
                            textAlign = TextAlign.Center
                        )

                        @Composable
                        fun Button() {
                            var isPressed: Boolean by remember { mutableStateOf(value = false) }

                            val buttonElevation: Int by animateIntAsState(
                                targetValue = if (isPressed) 0 else 1,
                                label = "button elevation interaction"
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onPress = {
                                                isPressed = true
                                                tryAwaitRelease()
                                                isPressed = false
                                                navController.navigate(route = Destination.PermissionRequestScreen.route)
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.button_label_next),
                                    style = TextStyle(
                                        fontFamily = robotoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        lineHeight = 28.sp,
                                        textAlign = TextAlign.Center
                                    ).notScale(),
                                    color = if (isDarkMode)
                                        onPrimaryContainerDark
                                    else
                                        onPrimaryContainerLight
                                )
                            }
                        }
                        Button()
                    }
                }
                Main()
            }
        }
        Content()
    }
}