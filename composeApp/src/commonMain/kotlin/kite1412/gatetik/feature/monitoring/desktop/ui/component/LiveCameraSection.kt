package kite1412.gatetik.feature.monitoring.desktop.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.data
import org.jetbrains.compose.resources.painterResource

@Composable
fun LiveCameraSection(
    cctvs: LoadState<List<Cctv>>,
    modifier: Modifier = Modifier,
    showFullScreenButton: Boolean = false,
    onFullScreenClick: (Cctv, autoMicOn: Boolean) -> Unit = {_, _ ->}
) {
    var currentIndex by retain { mutableIntStateOf(0) }
    val currentCctv = cctvs.data?.getOrNull(currentIndex)

    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier.background(Color.Black)
            ) {
                val videoModifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)

                currentCctv?.let { cctv ->
                    WebRtcPlayer(
                        url = getWebRtcStreamUrl(cctv.path),
                        modifier = videoModifier
                    )
                } ?: Box(videoModifier)

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
                            text = currentCctv?.cameraName ?: "~",
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
                Column {
                    Text(
                        text = "KAMERA LANGSUNG",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp
                        )
                    )
                    currentCctv?.let { cctv ->
                        Text(
                            text = cctv.cameraName,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val iconButtonModifier: @Composable (() -> Unit) -> Modifier = { onClick ->
                        Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onClick)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                            .padding(16.dp)
                            .size(20.dp)
                    }

                    val cctvListSize = cctvs.data?.size ?: 0

                    AnimatedVisibility(
                        visible = currentCctv?.type == CctvType.INTERCOM
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.mic),
                            contentDescription = "interkom",
                            modifier = iconButtonModifier {
                                currentCctv?.let { onFullScreenClick(it, true) }
                            }
                        )
                    }

                    if (cctvListSize > 1) Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.chevronRight),
                            contentDescription = "Previous",
                            modifier = iconButtonModifier {
                                currentIndex = (currentIndex - 1 + cctvListSize) % cctvListSize
                            }
                                .rotate(180f),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            painter = painterResource(GateTikIcons.chevronRight),
                            contentDescription = "Next",
                            modifier = iconButtonModifier {
                                currentIndex = (currentIndex + 1) % cctvListSize
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (showFullScreenButton) Icon(
                        painter = painterResource(GateTikIcons.zoomIn),
                        contentDescription = "Full Screen",
                        modifier = iconButtonModifier {
                            currentCctv?.let { onFullScreenClick(it, false) }
                        },
                        tint = Blue500
                    )
                }
            }
        }
    }
}
