package kite1412.portaltik.ui.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController

@Composable
fun Modifier.consumeSideNavBarWidth() = padding(
    start = LocalScaffoldComponentsController
        .current
        .getState(ScaffoldComponent.NAV_BAR)
        .size.width
)