package kite1412.gatetik.feature.monitoring.desktop.usermanagement

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopUserManagementScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.UserManagement.name) {
        DesktopUserManagementScreen(
            contentPadding = contentPadding
        )
    }
}