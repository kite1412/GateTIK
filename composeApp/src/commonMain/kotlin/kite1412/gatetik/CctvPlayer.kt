package kite1412.gatetik

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.gatetik.ui.util.LoadState

@Composable
expect fun CctvPlayer(modifier: Modifier = Modifier, state: (LoadState<Unit>) -> Unit)