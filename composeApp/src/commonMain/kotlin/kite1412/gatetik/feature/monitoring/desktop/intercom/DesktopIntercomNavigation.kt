package kite1412.gatetik.feature.monitoring.desktop.intercom

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopIntercomScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Intercom.route) {
        DesktopIntercomScreen(
            contentPadding = contentPadding
        )
    }
}