package kite1412.portaltik.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.White55

@Composable
fun SmallCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = White55
) {
    CircularProgressIndicator(
        modifier = modifier.size(12.dp),
        strokeWidth = 2.dp,
        color = color
    )
}