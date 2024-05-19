package app.kotlin.snapnote.ui.views

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.kotlin.snapnote.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun DraftScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val image: AnimatedImageVector = AnimatedImageVector
            .animatedVectorResource(id = R.drawable.rotate_gear)

        var atEnd: Boolean by remember { mutableStateOf(value = false) }

        LaunchedEffect(Unit) {
            while (true) {
                atEnd = !atEnd
                delay(timeMillis = 4000L)
            }
        }

        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "Timer"
        )
    }
}