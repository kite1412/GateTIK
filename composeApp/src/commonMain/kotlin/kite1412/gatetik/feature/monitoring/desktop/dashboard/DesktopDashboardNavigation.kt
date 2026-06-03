package kite1412.gatetik.feature.monitoring.desktop.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopDashboardScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Dashboard.name) {
        DesktopDashboardScreen(contentPadding = contentPadding)
    }
}