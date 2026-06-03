package kite1412.gatetik.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray900
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White60
import kite1412.gatetik.designsystem.util.GateTikIcons
import org.jetbrains.compose.resources.painterResource

@Composable
fun GateControlButton(
    isOpen: Boolean,
    actionEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundBrush: Brush = SolidColor(White),
    disabledBackgroundBrush: Brush = SolidColor(White60),
    color: Color = Blue500,
    disabledColor: Color = Gray900.copy(alpha = 0.5f),
    contentPadding: PaddingValues = PaddingValues(
        vertical = 12.dp
    )
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(if (actionEnabled) backgroundBrush else disabledBackgroundBrush)
            .clickable(
                enabled = actionEnabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = Color.Black.copy(alpha = 0.4f)
                )
            ) {
                if (actionEnabled) onClick()
            }
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val color = if (actionEnabled) color else disabledColor

            Icon(
                painter = painterResource(
                    if (isOpen) GateTikIcons.doorOpen else GateTikIcons.doorClose
                ),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = color
            )
            Text(
                text = (if (isOpen) "BUKA" else "TUTUP") + " GATE",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
        }
    }
}

@Preview
@Composable
private fun GateControlButtonPreview() {
    GateTikTheme {
        Scaffold { p ->
              GateControlButton(
                  isOpen = true,
                  actionEnabled = false,
                  onClick = {},
                  modifier = Modifier.padding(p)
              )
        }
    }
}