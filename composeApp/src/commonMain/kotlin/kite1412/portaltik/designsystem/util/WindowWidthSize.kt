package kite1412.portaltik.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowWidthSize(): WindowWidthSize {
    val width = LocalWindowInfo.current.containerDpSize.width

    // breakpoints ref:
    // https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes
    return when {
        width < 600.dp -> WindowWidthSize.COMPACT
        width >= 600.dp && width < 1200.dp -> WindowWidthSize.MEDIUM
        else -> WindowWidthSize.LARGE
    }
}

enum class WindowWidthSize {
    COMPACT,
    MEDIUM,
    LARGE
}