package kite1412.gatetik.ui.navigation

import kite1412.gatetik.designsystem.component.Destination
import org.jetbrains.compose.resources.DrawableResource

interface RootDestination {
    val route: String
    val icon: DrawableResource
    val label: String
}

fun RootDestination.toNavBarDestination() = Destination(
    route = route,
    label = label,
    icon = icon
)