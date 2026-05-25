package kite1412.portaltik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.ui.util.LoadState

@Composable
actual fun CctvPlayer(
    modifier: Modifier,
    state: (LoadState<Unit>) -> Unit
) {
}