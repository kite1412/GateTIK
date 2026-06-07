package kite1412.gatetik.feature.monitoring.desktop.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.gatetik.CctvPlayer
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.util.GateTikIcons
import org.jetbrains.compose.resources.painterResource

@Composable
fun LiveCameraSection(
    cameraName: String,
    modifier: Modifier = Modifier,
    showFullScreenButton: Boolean = false,
    onFullScreenClick: () -> Unit = {},
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                CctvPlayer(
                    modifier = Modifier.fillMaxSize()
                ) {}

                Badge(
                    text = "LIVE",
                    containerColor = Red500,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp),
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(GateTikIcons.videoRecorder),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cameraName,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KAMERA LANGSUNG",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showFullScreenButton) Text(
                    text = "Layar Penuh ↗",
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = onFullScreenClick
                    ),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Blue500
                )
            }
        }
    }
}