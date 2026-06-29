package kite1412.gatetik.feature.monitoring.desktop.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.AppWindow
import kite1412.gatetik.MicLevelMonitor
import kite1412.gatetik.WebRtcPlayer
import kite1412.gatetik.designsystem.component.AudioLevelMeter
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.getWebRtcStreamUrl
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.component.ActionIconButton

@Composable
fun CctvWindow(
    cctv: Cctv, onClose: () -> Unit,
    autoMicOn: Boolean = false
) {
    var isMicOn by retain { mutableStateOf(autoMicOn) }
    val micLevel by MicLevelMonitor.level.collectAsStateWithLifecycle()

    LaunchedEffect(isMicOn) {
        if (isMicOn) MicLevelMonitor.start() else MicLevelMonitor.stop()
    }

    AppWindow(
        title = cctv.cameraName,
        onClose = {
            MicLevelMonitor.stop()
            onClose()
        }
    ) {
        GateTikTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .radialBackground()
            ) {
                WebRtcPlayer(
                    url = getWebRtcStreamUrl(cctv.path) + if (isMicOn) "&media=video+audio+microphone" else "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = cctv.cameraName,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Badge(
                                text = "ID: ${cctv.id}",
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Badge(
                                text = cctv.type.name,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            if (cctv.isActive) {
                                Badge(
                                    text = "ACTIVE",
                                    containerColor = Emerald500.copy(alpha = 0.2f),
                                    contentColor = Emerald500
                                )
                            }
                        }
                    }

                    if (cctv.type == CctvType.INTERCOM) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isMicOn) {
                                AudioLevelMeter(level = micLevel)
                            }
                            Text(
                                text = if (isMicOn) "MIC ON" else "MIC OFF",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isMicOn) Emerald500 else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            ActionIconButton(
                                icon = if (isMicOn) GateTikIcons.mic else GateTikIcons.micOff,
                                onClick = { isMicOn = !isMicOn },
                                containerColor = if (isMicOn) Emerald500.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant,
                                tint = if (isMicOn) Emerald500 else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    ActionIconButton(
                        icon = GateTikIcons.x,
                        onClick = onClose,
                        containerColor = Red500.copy(alpha = 0.1f),
                        tint = Red500
                    )
                }
            }
        }
    }
}
