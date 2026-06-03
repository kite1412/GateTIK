package kite1412.gatetik.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import gatetik.composeapp.generated.resources.Res
import gatetik.composeapp.generated.resources.inter_italic_variablefont_opsz_wght
import gatetik.composeapp.generated.resources.inter_variablefont_opsz_wght

private const val NORMAL_LINE_HEIGHT_MULTIPLIER = 1.4f
private val Inter @Composable get() = FontFamily(
    Font(
        resource = Res.font.inter_variablefont_opsz_wght,
        style = FontStyle.Normal
    ),
    Font(
        resource = Res.font.inter_italic_variablefont_opsz_wght,
        style = FontStyle.Italic
    )
)

val Typography @Composable get() = Typography(
    bodySmall = interTextStyle(12),
    bodyMedium = interTextStyle(16),
    bodyLarge = interTextStyle(20),
    labelSmall = interTextStyle(12).bold(),
    labelMedium = interTextStyle(14).bold(),
    labelLarge = interTextStyle(16).bold(),
    titleSmall = interTextStyle(20).bold(),
    titleMedium = interTextStyle(24).bold(),
    titleLarge = interTextStyle(28).bold()
)

@Composable
private fun interTextStyle(fontSize: Int) = TextStyle(
    fontFamily = Inter,
    fontSize = fontSize.sp,
    lineHeight = (fontSize * NORMAL_LINE_HEIGHT_MULTIPLIER).sp,
    fontWeight = FontWeight.Normal
)

private fun TextStyle.bold() = copy(fontWeight = FontWeight.Bold)