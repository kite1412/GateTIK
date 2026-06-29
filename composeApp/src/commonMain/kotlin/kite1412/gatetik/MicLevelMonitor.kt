package kite1412.gatetik

import kotlinx.coroutines.flow.StateFlow

expect object MicLevelMonitor {
    // 0.0 - 1.0
    val level: StateFlow<Float>

    fun start()
    fun stop()
}