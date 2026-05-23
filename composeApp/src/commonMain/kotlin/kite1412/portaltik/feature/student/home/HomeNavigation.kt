package kite1412.portaltik.feature.student.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.student.StudentGraph

fun NavGraphBuilder.homeScreen() {
    composable(StudentGraph.Home.name) {
        HomeScreen()
    }
}