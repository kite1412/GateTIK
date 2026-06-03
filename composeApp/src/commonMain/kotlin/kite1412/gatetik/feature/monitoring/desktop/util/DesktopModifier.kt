package kite1412.gatetik.feature.monitoring.desktop.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.gatetik.ui.util.consumeSideNavBarWidth

@Composable
fun Modifier.desktopBaseModifier() =
    fillMaxSize()
        .consumeSideNavBarWidth()