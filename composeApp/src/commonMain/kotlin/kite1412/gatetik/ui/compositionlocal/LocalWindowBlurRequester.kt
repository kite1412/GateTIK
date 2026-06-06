package kite1412.gatetik.ui.compositionlocal

import androidx.compose.runtime.compositionLocalOf

val LocalWindowBlurRequester = compositionLocalOf<WindowBlurRequester> {
    throw NotImplementedError("WindowBlurRequester is not initialized")
}

interface WindowBlurRequester {
    fun applyWindowBlur()

    fun removeWindowBlue()
}
