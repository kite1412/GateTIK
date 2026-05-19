package kite1412.portaltik.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import portaltik.composeapp.generated.resources.Res
import portaltik.composeapp.generated.resources.inter_italic_variablefont_opsz_wght
import portaltik.composeapp.generated.resources.inter_variablefont_opsz_wght

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
    labelSmall = interTextStyle(12),
    labelMedium = interTextStyle(14),
    labelLarge = interTextStyle(16),
    titleSmall = interTextStyle(20).copy(
        fontWeight = FontWeight.Bold
    ),
    titleMedium = interTextStyle(24).copy(
        fontWeight = FontWeight.Bold
    ),
    titleLarge = interTextStyle(28).copy(
        fontWeight = FontWeight.Bold
    )
)

@Composable
private fun interTextStyle(fontSize: Int) = TextStyle(
    fontFamily = Inter,
    fontSize = fontSize.sp,
    lineHeight = (fontSize * NORMAL_LINE_HEIGHT_MULTIPLIER).sp,
    fontWeight = FontWeight.Normal
)

