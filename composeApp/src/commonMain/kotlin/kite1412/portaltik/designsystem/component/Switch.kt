package kite1412.portaltik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Black70
import kite1412.portaltik.designsystem.theme.Blue100_70
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.Gray900
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.White

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 50.dp,
    height: Dp = width / 1.7f,
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    val background by animateColorAsState(
        targetValue = if (checked) Blue500 else if (isDarkMode) Gray900 else Blue100_70
    )
    val density = LocalDensity.current
    val padding = 2.dp
    val thumbSize = height - (padding * 2)
    val offsetX by animateIntOffsetAsState(
        targetValue = IntOffset(
            x = with(density) {
                if (checked) (width - thumbSize - padding * 2).roundToPx()
                else 0
            },
            y = 0
        ),
        animationSpec = tween(300)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .clickable {
                onCheckedChange(!checked)
            }
            .background(background)
            .padding(padding)
    ) {
        Box(
            Modifier
                .offset { offsetX }
                .fillMaxHeight()
                .aspectRatio(1f)
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 8.dp,
                        color = Black70.copy(0.2f),
                        offset = DpOffset(
                            x = 0.dp,
                            y = 4.dp
                        )
                    )
                )
                .background(
                    color = White,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
private fun SwitchPreview() {
    var checked by remember { mutableStateOf(false) }

    PortalTikTheme {
       Scaffold { p ->
             Switch(
                 checked = checked,
                 onCheckedChange = { checked = it },
                 modifier = Modifier.padding(p)
             )
       }
   }
}