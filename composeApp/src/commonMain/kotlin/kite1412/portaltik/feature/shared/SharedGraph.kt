package kite1412.portaltik.feature.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.app.largeContentPadding
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.shared.authentication.navigation.authenticationScreen

fun NavGraphBuilder.sharedGraph(scaffoldPadding: PaddingValues) {
    navigation(
        startDestination = SharedGraph.AuthenticationRoute.name,
        route = SharedGraph.route
    ) {
        authenticationScreen(contentPadding = largeContentPadding(scaffoldPadding))
    }
}

object SharedGraph : Graph {
    override val route: String = "shared_graph"

    object AuthenticationRoute : Route("auth")
}