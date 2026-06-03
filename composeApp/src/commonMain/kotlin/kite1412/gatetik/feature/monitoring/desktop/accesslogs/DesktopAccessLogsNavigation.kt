package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopAccessLogsScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.AccessLogs.name) {
        DesktopAccessLogsScreen(
            contentPadding = contentPadding
        )
    }
}