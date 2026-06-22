package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebRtcPlayer(url: String, modifier: Modifier = Modifier)

fun getWebRtcStreamUrl(path: String) = BuildConfig.WEB_RTC_PLAYER_BASE_URL + path