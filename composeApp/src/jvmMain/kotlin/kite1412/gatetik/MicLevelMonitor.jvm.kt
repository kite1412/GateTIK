package kite1412.gatetik

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.log10
import kotlin.math.sqrt

actual object MicLevelMonitor {
    private val _level = MutableStateFlow(0f)
    actual val level: StateFlow<Float> = _level.asStateFlow()

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    actual fun start() {
        if (job != null) return

        job = scope.launch {
            val format = AudioFormat(44100f, 16, 1, true, false)
            val info = DataLine.Info(TargetDataLine::class.java, format)

            if (!AudioSystem.isLineSupported(info)) {
                return@launch
            }

            val line = AudioSystem.getLine(info) as TargetDataLine
            line.open(format)
            line.start()

            val buffer = ByteArray(line.bufferSize / 5)
            
            try {
                while (isActive) {
                    val readSize = line.read(buffer, 0, buffer.size)
                    if (readSize > 0) {
                        var sum = 0.0
                        for (i in 0 until readSize step 2) {
                            // 16-bit PCM little endian
                            val low = buffer[i].toInt()
                            val high = buffer[i + 1].toInt()
                            val sample = (high shl 8) or (low and 0xFF)
                            sum += sample * sample
                        }
                        
                        val rms = sqrt(sum / (readSize / 2))
                        val db = if (rms > 0) 20 * log10(rms / 32768.0) else -100.0
                        val normalized = ((db + 60) / 60).coerceIn(0.0, 1.0).toFloat()
                        
                        _level.value = normalized
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                line.stop()
                line.close()
                _level.value = 0f
            }
        }
    }

    actual fun stop() {
        job?.cancel()
        job = null
        _level.value = 0f
    }
}
