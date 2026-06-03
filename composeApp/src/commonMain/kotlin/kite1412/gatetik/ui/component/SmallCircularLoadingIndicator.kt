package kite1412.gatetik.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Black70
import kite1412.gatetik.designsystem.theme.White55
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode

@Composable
fun SmallCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = if (LocalDarkMode.current) White55 else Black70
) {
    CircularProgressIndicator(
        modifier = modifier.size(12.dp),
        strokeWidth = 2.dp,
        color = color
    )
}