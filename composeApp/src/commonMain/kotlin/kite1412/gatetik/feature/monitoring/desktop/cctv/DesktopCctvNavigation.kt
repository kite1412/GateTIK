package kite1412.gatetik.feature.monitoring.desktop.cctv

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopCctvScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Cctv.name) {
        DesktopCctvScreen(
            contentPadding = contentPadding
        )
    }
}