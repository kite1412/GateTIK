package kite1412.portaltik.feature.admin.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph
import kite1412.portaltik.feature.admin.dashboard.DashboardScreen

fun NavGraphBuilder.dashboardScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Dashboard.name) {
        DashboardScreen(contentPadding = contentPadding)
    }
}