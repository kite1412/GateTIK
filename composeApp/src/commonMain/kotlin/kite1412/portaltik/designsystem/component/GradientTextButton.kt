package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Blue500Alt
import kite1412.portaltik.designsystem.theme.BluePurpleLinearGradient
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.White

@Composable
fun GradientTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 2.dp,
                    color = Blue500Alt.copy(alpha = 0.4f),
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .background(
                brush = Brush.linearGradient(BluePurpleLinearGradient),
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        leading?.invoke()
        Text(
            text = text,
            color = White
        )
    }
}

@Preview
@Composable
private fun GradientTextButtonPreview() {
    PortalTikTheme {
        Scaffold { p ->
              GradientTextButton(
                  text = "A button",
                  onClick = {},
                  modifier = Modifier
                      .padding(p)
                      .padding(24.dp)
              )
        }
    }
}