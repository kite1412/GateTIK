package kite1412.gatetik

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.sqrt

actual object MicLevelMonitor {
    private val _level = MutableStateFlow(0f)
    actual val level: StateFlow<Float> = _level.asStateFlow()

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    private const val SAMPLE_RATE = 44100
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

    @SuppressLint("MissingPermission")
    actual fun start() {
        if (job != null) return

        job = scope.launch {
            val audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                BUFFER_SIZE
            )

            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
                return@launch
            }

            audioRecord.startRecording()
            val buffer = ShortArray(BUFFER_SIZE)

            while (isActive) {
                val readSize = audioRecord.read(buffer, 0, buffer.size)
                if (readSize > 0) {
                    var sum = 0.0
                    for (i in 0 until readSize) {
                        sum += buffer[i] * buffer[i]
                    }
                    val rms = sqrt(sum / readSize)
                    val db = if (rms > 0) 20 * log10(rms / 32767.0) else -100.0
                    val normalized = ((db + 60) / 60).coerceIn(0.0, 1.0).toFloat()

                    _level.value = normalized
                }
            }

            audioRecord.stop()
            audioRecord.release()
            _level.value = 0f
        }
    }

    actual fun stop() {
        job?.cancel()
        job = null
        _level.value = 0f
    }
}
