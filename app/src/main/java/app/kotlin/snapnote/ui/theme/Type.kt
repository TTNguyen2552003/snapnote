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

val displayLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 57.sp,
    lineHeight = 64.sp,
    letterSpacing = (0.25).sp
)

val displayMedium = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 45.sp,
    lineHeight = 52.sp
)

val displaySmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 36.sp,
    lineHeight = 44.sp
)

val headlineLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 32.sp,
    lineHeight = 40.sp
)

val headlineMedium = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 28.sp,
    lineHeight = 36.sp
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

val labelLargeProminent = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (0.1).sp
)

val labelLarge = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (0.1).sp
)

val labelMediumProminent = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = (0.5).sp
)

val labelMedium = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = (0.5).sp
)

val labelSmall = TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = (0.5).sp
)