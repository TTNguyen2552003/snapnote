package app.kotlin.snapnote.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.kotlin.snapnote.R


@Composable
fun TextStyle.notScale(): TextStyle {
    val fontScale: Float = LocalConfiguration.current.fontScale
    return this.copy(
        fontSize = this.fontSize / fontScale,
        letterSpacing = if (this.letterSpacing.value.isNaN())
            0.sp
        else
            (this.letterSpacing.value / fontScale).sp
    )
}

val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)


val headlineSmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 32.sp
)

val titleLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp
)

val titleMedium = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (0.15).sp
)

val titleSmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (0.1).sp
)

val bodyLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (0.5).sp
)

val bodyMedium = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (0.25).sp
)

val bodySmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp
)


val labelLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (0.1).sp
)


val labelSmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = (0.5).sp
)