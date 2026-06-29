package kite1412.gatetik

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual object MicLevelMonitor {
    private val _level = MutableStateFlow(0f)
    actual val level: StateFlow<Float> = _level.asStateFlow()

    actual fun start() {
        // Not yet implemented for desktop
    }

    actual fun stop() {
        // Not yet implemented for desktop
    }
}
