package kite1412.portaltik.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue200_60
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White10
import kite1412.portaltik.designsystem.theme.White15
import kite1412.portaltik.designsystem.util.GateTikIcons
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import org.jetbrains.compose.resources.painterResource

@Composable
fun ThemeToggle(
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current,
    iconSize: Dp = 24.dp,
    clip: Shape = CircleShape
) {
    val toggleBorderColor by animateColorAsState(if (isDarkMode) White15 else Blue200_60)
    val toggleBackgroundColor by animateColorAsState(if (isDarkMode) White10 else White)
    val toggleIconColor by animateColorAsState(if (isDarkMode) White else Blue500)

    Box(
        modifier = modifier
            .size(iconSize * 2)
            .border(
                width = 1.dp,
                color = toggleBorderColor,
                shape = clip
            )
            .background(
                color = toggleBackgroundColor,
                shape = clip
            )
            .clip(clip)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onToggle
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(if (isDarkMode) GateTikIcons.sun else GateTikIcons.moon),
            contentDescription = "Toggle Theme",
            tint = toggleIconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}