package kite1412.portaltik.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

private val LightColorScheme = lightColorScheme(
    primary = Blue100,
    onPrimary = Blue900,
    background = White,
    surface = White,
    onSurface = Slate900,
    onSurfaceVariant = Slate500,
    outline = Blue200
)
private val DarkColorScheme = darkColorScheme(
    primary = Blue900,
    onPrimary = Blue100,
    background = Slate950_100,
    surface = Slate900_95,
    onSurface = Gray200,
    onSurfaceVariant = Slate400,
    outline = Slate800
)

@Composable
fun PortalTikTheme(
    darkTheme: Boolean = LocalDarkMode.current,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        ProvideTextStyle(Typography.bodyMedium) {
            content()
        }
    }
}