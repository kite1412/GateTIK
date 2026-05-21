package kite1412.portaltik.feature.admin

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.DeviceType
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.admin.desktop.dashboard.navigation.desktopAdminDashboardScreen
import kite1412.portaltik.feature.admin.mobile.home.navigation.mobileAdminHomeScreen
import kite1412.portaltik.getDeviceType
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider
import org.jetbrains.compose.resources.DrawableResource

fun NavGraphBuilder.adminGraph(contentPadding: PaddingValues) {
    val isDesktop = getDeviceType() == DeviceType.DESKTOP

    navigation(
        startDestination = if (isDesktop) AdminGraph.Desktop.Dashboard.name
            else AdminGraph.Mobile.Home.name,
        route = AdminGraph.route
    ) {
        if (isDesktop) desktopAdminGraph(contentPadding)
        else mobileAdminGraph(contentPadding)
    }
}

object AdminGraph : Graph {
    override val route: String = "admin_graph"

    object Desktop : RootDestinationsProvider {
        override val rootDestinations: List<RootDestination> = listOf(Dashboard)

        object Dashboard : RootDestination, Route("desktop_admin_dashboard") {
            override val route: String = name
            override val icon: DrawableResource = PortalTikIcons.dashboard
            override val label: String = "Dashboard"
        }
    }

    object Mobile : RootDestinationsProvider {
        override val rootDestinations: List<RootDestination> = listOf(Home)

        object Home : RootDestination, Route("mobile_admin_home") {
            override val route: String = name
            override val icon: DrawableResource = PortalTikIcons.house
            override val label: String = "Home"
        }
    }
}

private fun NavGraphBuilder.desktopAdminGraph(contentPadding: PaddingValues) {
    desktopAdminDashboardScreen(contentPadding = contentPadding)
}

private fun NavGraphBuilder.mobileAdminGraph(contentPadding: PaddingValues) {
    mobileAdminHomeScreen(contentPadding = contentPadding)
}