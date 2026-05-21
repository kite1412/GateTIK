package kite1412.portaltik.feature.admin

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.admin.dashboard.navigation.dashboardScreen

fun NavGraphBuilder.adminGraph(contentPadding: PaddingValues) {
    navigation(
        startDestination = AdminGraph.Dashboard.name,
        route = AdminGraph.route
    ) {
        dashboardScreen(contentPadding = contentPadding)
    }
}

object AdminGraph : Graph {
    override val route: String = "admin_graph"

    object Dashboard : Route("dashboard")
}