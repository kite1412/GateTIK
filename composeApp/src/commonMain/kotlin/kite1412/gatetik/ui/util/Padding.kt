package kite1412.gatetik.ui.util

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController

@Composable
fun Modifier.consumeSideNavBarWidth(): Modifier {
    val navBar = LocalScaffoldComponentsController
        .current
        .getState(ScaffoldComponent.NAV_BAR)
    val startPadding by animateDpAsState(
        targetValue = if (navBar.isVisible) navBar.size.width else 0.dp
    )

    return padding(start = startPadding)
}