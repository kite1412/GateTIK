package kite1412.portaltik.feature.student

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.app.smallContentPadding
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.shared.SharedGraph
import kite1412.portaltik.feature.shared.profile.profileScreen
import kite1412.portaltik.feature.student.gateaccess.gateAccessScreen
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider
import org.jetbrains.compose.resources.DrawableResource

fun NavGraphBuilder.studentGraph(scaffoldPadding: PaddingValues) {
    navigation(
        startDestination = StudentGraph.GateAccess.route,
        route = StudentGraph.route
    ) {
        gateAccessScreen(contentPadding = smallContentPadding(scaffoldPadding))
        profileScreen(contentPadding = smallContentPadding(scaffoldPadding))
    }
}

object StudentGraph : RootDestinationsProvider, Graph {
    override val route: String = "student_graph"
    override val rootDestinations: List<RootDestination> = listOf(GateAccess, Profile)

    object GateAccess : RootDestination, Route("gate_access") {
        override val route: String = name
        override val icon: DrawableResource = PortalTikIcons.doorOpen
        override val label: String = "Gate"
    }

    object Profile : RootDestination {
        override val route: String = SharedGraph.ProfileRoute.name
        override val icon: DrawableResource = PortalTikIcons.person
        override val label: String = "Profil"
    }
}