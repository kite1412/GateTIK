package kite1412.portaltik.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Black70
import kite1412.portaltik.designsystem.theme.White55
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

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