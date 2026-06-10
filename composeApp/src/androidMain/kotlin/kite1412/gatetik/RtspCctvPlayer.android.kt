package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.vlc.VlcProvider
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

@Composable
actual fun RtspCctvPlayer(
    modifier: Modifier,
    state: (LoadState<Unit>) -> Unit
) {
    AndroidView(
        factory = {
            VLCVideoLayout(it).also { layout ->
                val mediaPlayer = VlcProvider.createMediaPlayer()
                mediaPlayer.media = VlcProvider.getMedia(BuildConfig.RTSP_CCTV_URL)
                mediaPlayer.setEventListener { event ->
                    when (event.type) {
                        MediaPlayer.Event.Buffering -> state(LoadState.Loading())
                        MediaPlayer.Event.Playing -> state(LoadState.Success(Unit))
                        MediaPlayer.Event.EncounteredError -> state(LoadState.Error(""))
                    }
                }
                mediaPlayer.attachViews(layout, null, false, false)
                mediaPlayer.play()
            }
        },
        modifier = modifier
    )
}