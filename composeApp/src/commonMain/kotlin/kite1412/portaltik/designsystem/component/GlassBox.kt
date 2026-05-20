package kite1412.portaltik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Blue200_60
import kite1412.portaltik.designsystem.theme.White10
import kite1412.portaltik.designsystem.theme.White15
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val isDarkMode = LocalDarkMode.current
    val borderColor by animateColorAsState(if (!isDarkMode) Blue200_60 else White15)
    val backgroundColor by animateColorAsState(if (!isDarkMode) White60 else White10)
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .border(
                width = 2.dp,
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
