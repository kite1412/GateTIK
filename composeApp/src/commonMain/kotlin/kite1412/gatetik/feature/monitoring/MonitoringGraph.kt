package kite1412.gatetik.feature.monitoring

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.gatetik.PlatformType
import kite1412.gatetik.app.normalContentPadding
import kite1412.gatetik.app.smallContentPadding
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.Graph
import kite1412.gatetik.feature.Route
import kite1412.gatetik.feature.monitoring.desktop.cctv.desktopCctvScreen
import kite1412.gatetik.feature.monitoring.desktop.dashboard.desktopDashboardScreen
import kite1412.gatetik.feature.monitoring.desktop.parking.desktopParkingScreen
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.desktopUserManagementScreen
import kite1412.gatetik.feature.monitoring.mobile.cctv.mobileCctvScreen
import kite1412.gatetik.feature.monitoring.mobile.home.mobileHomeScreen
import kite1412.gatetik.feature.monitoring.mobile.parking.mobileParkingScreen
import kite1412.gatetik.feature.shared.SharedGraph
import kite1412.gatetik.feature.shared.profile.profileScreen
import kite1412.gatetik.getPlatform
import kite1412.gatetik.ui.navigation.RootDestination
import kite1412.gatetik.ui.navigation.RootDestinationsProvider
import org.jetbrains.compose.resources.DrawableResource

fun NavGraphBuilder.monitoringGraph(
    scaffoldPadding: PaddingValues,
    navigateToRootDestination: (RootDestination) -> Unit
) {
    val isDesktop = getPlatform().type == PlatformType.DESKTOP

    navigation(
        startDestination = if (isDesktop) MonitoringGraph.Desktop.Dashboard.name
            else MonitoringGraph.Mobile.Home.name,
        route = MonitoringGraph.route
    ) {
        if (isDesktop) desktopMonitoringGraph(scaffoldPadding)
        else mobileMonitoringGraph(
            scaffoldPadding = scaffoldPadding,
            navigateToRootDestination = navigateToRootDestination
        )
    }
}

object MonitoringGraph : Graph {
    override val route: String = "monitoring_graph"

    object Desktop : RootDestinationsProvider {
        override val rootDestinations: List<RootDestination> = listOf(
            Dashboard, Cctv, Parking, UserManagement, AccessLogs, VisitorIntercom
        )

        object Dashboard : RootDestination, Route("desktop_monitoring_dashboard") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.dashboard
            override val label: String = "Dashboard"
        }

        object Cctv : RootDestination, Route("desktop_monitoring_cctv") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.videoRecorder
            override val label: String = "CCTV Monitoring"
        }

        object Parking : RootDestination, Route("desktop_monitoring_parking") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.car
            override val label: String = "Manajemen Parkir"
        }

        object UserManagement : RootDestination, Route("desktop_monitoring_user_management") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.people
            override val label: String = "Manajemen Pengguna"
        }

        object AccessLogs : RootDestination, Route("desktop_monitoring_access_logs") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.scrollText
            override val label: String = "Access Log"
        }

        object VisitorIntercom : RootDestination, Route("desktop_monitoring_visitor_intercom") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.phoneCall
            override val label: String = "Interkom Pengunjung"
        }
    }

    object Mobile : RootDestinationsProvider {
        override val rootDestinations: List<RootDestination> = listOf(
            Home, Parking, Cctv, Profile
        )

        object Home : RootDestination, Route("mobile_monitoring_home") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.house
            override val label: String = "Home"
        }

        object Parking : RootDestination, Route("mobile_monitoring_parking") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.car
            override val label: String = "Parkir"
        }

        object Cctv : RootDestination, Route("mobile_monitoring_cctv") {
            override val route: String = name
            override val icon: DrawableResource = GateTikIcons.videoRecorder
            override val label: String = "CCTV"
        }

        object Profile : RootDestination {
            override val route: String = SharedGraph.ProfileRoute.name
            override val icon: DrawableResource = GateTikIcons.person
            override val label: String = "Profil"
        }
    }
}

private fun NavGraphBuilder.desktopMonitoringGraph(scaffoldPadding: PaddingValues) {
    desktopDashboardScreen(contentPadding = normalContentPadding(scaffoldPadding))
    desktopCctvScreen(contentPadding = normalContentPadding(scaffoldPadding))
    desktopParkingScreen(contentPadding = normalContentPadding(scaffoldPadding))
    desktopUserManagementScreen(contentPadding = normalContentPadding(scaffoldPadding))
}

private fun NavGraphBuilder.mobileMonitoringGraph(
    scaffoldPadding: PaddingValues,
    navigateToRootDestination: (RootDestination) -> Unit
) {
    mobileHomeScreen(
        contentPadding = smallContentPadding(scaffoldPadding),
        navigateToParking = { navigateToRootDestination(MonitoringGraph.Mobile.Parking) },
        navigateToCctv = { navigateToRootDestination(MonitoringGraph.Mobile.Cctv) }
    )
    mobileParkingScreen(contentPadding = smallContentPadding(scaffoldPadding))
    mobileCctvScreen(contentPadding = smallContentPadding(scaffoldPadding))
    profileScreen(contentPadding = smallContentPadding(scaffoldPadding))
}