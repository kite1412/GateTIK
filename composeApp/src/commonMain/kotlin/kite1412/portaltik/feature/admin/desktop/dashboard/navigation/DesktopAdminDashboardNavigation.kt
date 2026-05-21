package kite1412.portaltik.feature.admin.desktop.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph
import kite1412.portaltik.feature.admin.desktop.dashboard.DesktopAdminDashboardScreen

fun NavGraphBuilder.desktopAdminDashboardScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Desktop.Dashboard.name) {
        DesktopAdminDashboardScreen(contentPadding = contentPadding)
    }
}