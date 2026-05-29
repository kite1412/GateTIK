package kite1412.portaltik.feature.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.app.largeContentPadding
import kite1412.portaltik.app.smallContentPadding
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.shared.authentication.authenticationScreen
import kite1412.portaltik.feature.shared.profile.profileScreen
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider

fun NavGraphBuilder.sharedGraph(
    scaffoldPadding: PaddingValues,
    rootDestinationsProvider: RootDestinationsProvider?
) {
    navigation(
        startDestination = SharedGraph.AuthenticationRoute.name,
        route = SharedGraph.route
    ) {
        authenticationScreen(contentPadding = largeContentPadding(scaffoldPadding))
        optionalRoute(
            route = SharedGraph.ProfileRoute,
            rootDestinationsProvider = rootDestinationsProvider
        ) {
            profileScreen(contentPadding = smallContentPadding(scaffoldPadding))
        }
    }
}

object SharedGraph : Graph {
    override val route: String = "shared_graph"

    object AuthenticationRoute : Route("auth")

    object ProfileRoute : Route("profile")
}

/**
 * Conditionally includes a route in the navigation graph based on [RootDestinationsProvider].
 *
 * If [RootDestinationsProvider] is null or has no configured destinations,
 * the route is always included.
 *
 * Otherwise, the route is included only if it is not listed in the provider's
 * excluded destinations.
 */
private fun optionalRoute(
    rootDestinationsProvider: RootDestinationsProvider?,
    route: Route,
    routeContent: () -> Unit
) {
    if (rootDestinationsProvider?.rootDestinations?.isNotEmpty() == true) {
        if (
            !rootDestinationsProvider
                .rootDestinations
                .map(RootDestination::route)
                .contains(route.name)
        ) routeContent()
    } else routeContent()
}