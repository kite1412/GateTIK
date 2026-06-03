package kite1412.gatetik.feature.student

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.gatetik.app.smallContentPadding
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.Graph
import kite1412.gatetik.feature.Route
import kite1412.gatetik.feature.shared.SharedGraph
import kite1412.gatetik.feature.shared.profile.profileScreen
import kite1412.gatetik.feature.student.gateaccess.gateAccessScreen
import kite1412.gatetik.ui.navigation.RootDestination
import kite1412.gatetik.ui.navigation.RootDestinationsProvider
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
        override val icon: DrawableResource = GateTikIcons.doorOpen
        override val label: String = "Gate"
    }

    object Profile : RootDestination {
        override val route: String = SharedGraph.ProfileRoute.name
        override val icon: DrawableResource = GateTikIcons.person
        override val label: String = "Profil"
    }
}