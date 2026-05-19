package kite1412.portaltik.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    background = White,
    surface = White,
    onSurface = Slate900,
    onSurfaceVariant = Slate500,
    outline = Blue200
)
private val DarkColorScheme = darkColorScheme(
    background = Slate950_100,
    surface = Slate900_95,
    onSurface = Gray200,
    onSurfaceVariant = Slate400,
    outline = Slate800
)

@Composable
fun PortalTikTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}