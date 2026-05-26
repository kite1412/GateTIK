package kite1412.portaltik.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.model.Gate
import kite1412.portaltik.model.GateStatus
import kite1412.portaltik.ui.util.LoadState
import org.jetbrains.compose.resources.painterResource

@Composable
fun GateControlButton(
    isOpen: Boolean,
    gateState: LoadState<Gate?>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = Blue500
) {
    val actionEnabled = gateState is LoadState.Success && gateState.data != null &&
            gateState.data.currentStatus != GateStatus.OPENING &&
            gateState.data.currentStatus != GateStatus.OFFLINE
    val background by animateColorAsState(
        targetValue = if (actionEnabled) White else White60
    )
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 32.dp)
            .clip(shape)
            .background(background)
            .clickable(
                enabled = actionEnabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    color = Color.Black.copy(alpha = 0.4f)
                )
            ) {
                if (actionEnabled) onClick()
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        LoadState(
            state = gateState,
            loading = {
                SmallCircularProgressIndicator(color = color)
            },
            error = {
                Text("Gate tidak ditemukan")
            },
            success = { gate ->
                if (gate != null) Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isOpen) PortalTikIcons.doorOpen else PortalTikIcons.doorClose
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = color
                    )
                    if (gate.currentStatus != GateStatus.OPENING) Text(
                        text = (if (isOpen) "BUKA" else "TUTUP") + " GATE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = color
                    ) else SmallCircularProgressIndicator(color = color)
                } else Text("Gate tidak ditemukan")
            }
        )
    }
}

@Preview
@Composable
private fun GateControlButtonPreview() {
    PortalTikTheme {
        Scaffold { p ->
              GateControlButton(
                  isOpen = true,
                  gateState = LoadState.Loading(),
                  onClick = {},
                  modifier = Modifier.padding(p)
              )
        }
    }
}