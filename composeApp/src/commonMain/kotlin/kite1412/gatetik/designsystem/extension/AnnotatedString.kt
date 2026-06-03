package kite1412.gatetik.designsystem.extension

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kite1412.gatetik.designsystem.theme.Blue300
import kite1412.gatetik.designsystem.theme.Blue500

fun <R: Any> AnnotatedString.Builder.linkStyle(
    isDarkMode: Boolean,
    block: AnnotatedString.Builder.() -> R
): R = withStyle(
    style = SpanStyle(
        color = if (!isDarkMode) Blue500 else Blue300,
        fontWeight = FontWeight.Bold
    ),
    block = block
)