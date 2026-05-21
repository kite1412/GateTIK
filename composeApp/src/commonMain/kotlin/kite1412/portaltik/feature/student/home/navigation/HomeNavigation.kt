package kite1412.portaltik.feature.student.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.student.StudentGraph
import kite1412.portaltik.feature.student.home.HomeScreen

fun NavGraphBuilder.homeScreen() {
    composable(StudentGraph.Home.name) {
        HomeScreen()
    }
}