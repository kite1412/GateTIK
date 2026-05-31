package kite1412.portaltik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Gray200
import kite1412.portaltik.designsystem.theme.Slate900
import kite1412.portaltik.designsystem.theme.White30
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    isDarkMode: Boolean = LocalDarkMode.current,
    content: @Composable BoxScope.() -> Unit
) {
    val borderColor by animateColorAsState(if (isDarkMode) Gray200.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary)
    val backgroundColor by animateColorAsState(if (isDarkMode) Slate900.copy(alpha = 0.2f) else White30)
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
            .clip(shape)
            .padding(contentPadding),
        content = content
    )
}
