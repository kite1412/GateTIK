package kite1412.portaltik.feature.student

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kite1412.portaltik.feature.Graph
import kite1412.portaltik.feature.Route
import kite1412.portaltik.feature.student.home.navigation.homeScreen

fun NavGraphBuilder.studentGraph(contentPadding: PaddingValues) {
    navigation(
        startDestination = StudentGraph.Home.name,
        route = StudentGraph.route
    ) {
        homeScreen()
    }
}

object StudentGraph : Graph {
    override val route: String = "student_graph"

    object Home : Route("home")
}