package app.kotlin.snapnote.ui.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.kotlin.snapnote.Destination
import app.kotlin.snapnote.R
import app.kotlin.snapnote.ui.theme.bodyMedium
import app.kotlin.snapnote.ui.theme.bodySmall
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
import app.kotlin.snapnote.ui.theme.primaryDark
import app.kotlin.snapnote.ui.theme.primaryLight
import app.kotlin.snapnote.ui.theme.robotoFamily
import app.kotlin.snapnote.ui.theme.surfaceDark
import app.kotlin.snapnote.ui.theme.surfaceLight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestScreen(
    isDarkMode: Boolean = false,
    navController: NavHostController,
    saveFirstUse: () -> Unit
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
                    .padding(
                        start = 28.dp,
                        end = 28.dp
                    )
                    .align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "bells",
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
//                        Permission requestPermission sentence
                        Text(
                            text = stringResource(id = R.string.permission_request_sentence),
                            style = bodySmall.notScale(),
                            textAlign = TextAlign.Center,
                            color = if (isDarkMode)
                                onSurfaceDark
                            else
                                onSurfaceLight
                        )

                        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                        @Composable
                        fun Button() {
                            var isPress: Boolean by remember { mutableStateOf(value = false) }

                            val notificationPermissionState: PermissionState =
                                rememberPermissionState(
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.POST_NOTIFICATIONS
                                    else
                                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                                )

                            val buttonElevation: Int by animateIntAsState(
                                targetValue = if (isPress)
                                    0
                                else
                                    1,
                                label = "button elevation interaction"
                            )

                            var showRequestDialog: Boolean by remember {
                                mutableStateOf(value = false)
                            }

                            val requestPermission: () -> Unit = {
                                notificationPermissionState.launchPermissionRequest()
                            }

                            val context: Context = LocalContext.current

                            val goToSetting: () -> Unit = {
                                val intent: Intent =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)
                                    else
                                        Intent(Settings.ACTION_SETTINGS)
                                context.startActivity(intent)
                            }

                            val onDismissRequest: () -> Unit = {
                                showRequestDialog = false
                            }

                            if (!notificationPermissionState.status.isGranted && showRequestDialog) {
                                HandlePermissionConflict(
                                    isDarkMode = isDarkMode,
                                    notificationPermissionState = notificationPermissionState,
                                    requestPermission = requestPermission,
                                    goToSetting = goToSetting,
                                    onDismissRequest = onDismissRequest
                                )
                            }

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
                                                isPress = true
                                                tryAwaitRelease()
                                                isPress = false
                                                if (notificationPermissionState.status.isGranted) {
                                                    saveFirstUse()
                                                    navController.navigate(route = Destination.MainScreen.route)
                                                } else {
                                                    run {
                                                        requestPermission()
                                                        delay(timeMillis = 500L)
                                                        showRequestDialog = true
                                                    }
                                                }
                                            }
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.button_label_continue),
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

                @Composable
                fun TextButton() {
                    Text(
                        text = stringResource(id = R.string.button_label_back),
                        style = TextStyle(
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Center
                        )
                            .notScale(),
                        color = if (isDarkMode) outlineDark else outlineLight,
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { navController.popBackStack() }
                            )
                        }
                    )
                }
                TextButton()
            }
        }
        Content()
    }
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun HandlePermissionConflict(
    isDarkMode: Boolean = false,
    notificationPermissionState: PermissionState,
    requestPermission: () -> Unit,
    goToSetting: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Text(
                text = if (!notificationPermissionState.status.shouldShowRationale && !notificationPermissionState.status.isGranted)
                    "Setting"
                else
                    "Ok",
                style = labelLarge.notScale(),
                color = if (isDarkMode)
                    primaryDark
                else
                    primaryLight,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (notificationPermissionState.status.shouldShowRationale) {
                                onDismissRequest()
                                requestPermission()
                                delay(timeMillis = 500L)
                            } else {
                                onDismissRequest()
                                goToSetting()
                            }
                        }
                    )
                }
            )
        },
        title = {
            val text = AnnotatedString.Builder()
            text.append(text = "Allow ")
            text.withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append(text = "Notification")
            }
            val styledText: AnnotatedString = text.toAnnotatedString()
            Text(
                text = styledText,
                style = headlineSmall.notScale(),
                color = if (isDarkMode)
                    outlineDark
                else
                    onSurfaceLight
            )
        },
        text = {
            Text(
                text = "We don't want you to miss any notification from the app.",
                style = bodyMedium.notScale(),
                color = if (isDarkMode)
                    onSurfaceDark
                else
                    onSurfaceLight
            )
        },
        containerColor = if (isDarkMode)
            surfaceDark
        else
            surfaceLight,
        shape = RoundedCornerShape(size = 16.dp),
        icon = {
            val image: AnimatedImageVector = AnimatedImageVector
                .animatedVectorResource(
                    id = if (notificationPermissionState.status.shouldShowRationale)
                        R.drawable.ringing_bell_animation
                    else
                        R.drawable.rotate_gear
                )

            var atEnd: Boolean by remember { mutableStateOf(value = false) }

            LaunchedEffect(Unit) {
                while (true) {
                    atEnd = !atEnd
                    delay(timeMillis = 4000L)
                }
            }
            val size: Dp = if (notificationPermissionState.status.shouldShowRationale)
                64.dp
            else
                32.dp
            Image(
                painter = rememberAnimatedVectorPainter(image, atEnd),
                contentDescription = "Timer",
                modifier = Modifier
                    .width(width = size)
                    .height(height = size),
                contentScale = ContentScale.FillBounds
            )
        }
    )
}
