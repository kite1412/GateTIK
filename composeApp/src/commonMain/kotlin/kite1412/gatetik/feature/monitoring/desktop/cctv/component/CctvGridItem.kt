package kite1412.gatetik.feature.monitoring.desktop.cctv.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Slate500
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CctvGridItem(
    cctv: Cctv,
    onFullscreenClick: () -> Unit,
    onMicClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                WebRtcPlayer(
                    url = getWebRtcStreamUrl(cctv.path),
                    modifier = Modifier.fillMaxSize()
                )
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = Int.MAX_VALUE
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.camera),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Blue500
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = cctv.cameraName,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Badge(
                                text = when (cctv.type) {
                                    CctvType.MONITOR -> "Monitor"
                                    CctvType.INTERCOM -> "Interkom"
                                },
                                containerColor = Blue500.copy(alpha = 0.1f),
                                contentColor = Blue500
                            )
                        }
                        Text(
                            text = "/${cctv.path}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        icon = GateTikIcons.trash,
                        onClick = onDeleteClick,
                        containerColor = Red500
                    )
                    IconButton(
                        icon = GateTikIcons.settings,
                        onClick = onSettingsClick,
                        containerColor = Slate500
                    )
                    if (cctv.type == CctvType.INTERCOM) {
                        IconButton(
                            icon = GateTikIcons.mic,
                            onClick = onMicClick,
                            containerColor = Emerald500
                        )
                    }
                    IconButton(
                        icon = GateTikIcons.zoomIn,
                        onClick = onFullscreenClick,
                        containerColor = Blue500
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CctvGridItemPreview() {
    GateTikTheme {
        CctvGridItem(
            cctv = Cctv(
                id = 1,
                cameraName = "Camera 1",
                streamUrl = "url",
                path = "camera1",
                isActive = true,
                type = CctvType.MONITOR
            ),
            onFullscreenClick = {},
            onMicClick = {},
            onSettingsClick = {},
            onDeleteClick = {}
        )
    }
}

@Composable
private fun IconButton(
    icon: DrawableResource,
    onClick: () -> Unit,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(containerColor.copy(alpha = 0.8f))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = White
        )
    }
}
