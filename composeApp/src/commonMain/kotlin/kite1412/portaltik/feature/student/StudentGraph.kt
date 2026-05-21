package kite1412.portaltik.feature.student

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.student.home.navigation.homeScreen
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider
import org.jetbrains.compose.resources.DrawableResource

fun NavGraphBuilder.studentGraph(scaffoldPadding: PaddingValues) {
    navigation(
        startDestination = StudentGraph.Home.name,
        route = StudentGraph.route
    ) {
        homeScreen()
    }
}

object StudentGraph : RootDestinationsProvider, Graph {
    override val route: String = "student_graph"
    override val rootDestinations: List<RootDestination> = listOf(Home)

    object Home : RootDestination, Route("home") {
        override val route: String = name
        override val icon: DrawableResource = PortalTikIcons.house
        override val label: String = "Home"
    }
}