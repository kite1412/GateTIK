package kite1412.gatetik.designsystem.extension

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kite1412.gatetik.designsystem.theme.Blue400_40
import kite1412.gatetik.designsystem.theme.Blue500Alt_30
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Indigo400_30
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode

@Composable
fun Modifier.radialBackground(isDarkMode: Boolean = LocalDarkMode.current): Modifier {
    val color1 by animateColorAsState(if (isDarkMode) Blue500Alt_30 else Blue400_40)
    val color2 by animateColorAsState(if (isDarkMode) Indigo400_30 else Blue500Alt_30)

    return drawBehind {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(color1, Color.Transparent),
                center = Offset(size.width * 0.1f, size.height * 0.1f),
                radius = size.width * 0.8f
            )
        )
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(color2, Color.Transparent),
                center = Offset(size.width * 0.7f, size.height * 0.9f),
                radius = size.width * 0.9f
            )
        )
    }
}

@Preview
@Composable
private fun ModifierRadialBackgroundPreview() {
    GateTikTheme {
        Scaffold { p ->
              Box(
                  modifier = Modifier
                      .padding(p)
                      .fillMaxSize()
                      .radialBackground()
              )
        }
    }
}