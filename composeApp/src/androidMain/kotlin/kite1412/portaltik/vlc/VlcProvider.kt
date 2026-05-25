package kite1412.portaltik.vlc

import androidx.core.net.toUri
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

object VlcProvider : KoinComponent {
    private val vlc by inject<LibVLC>()
    private val cached = mutableMapOf<String, Media>()

    fun createMediaPlayer() = MediaPlayer(vlc)

    fun getMedia(url: String): Media = cached[url] ?: Media(vlc, url.toUri()).apply {
        setHWDecoderEnabled(true, false)
        addOption(":network-caching=300")
    }.also {
        cached[url] = it
    }
}