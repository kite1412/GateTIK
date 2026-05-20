package kite1412.portaltik.feature.student.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.student.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen() {
    composable<HomeRoute> {
        HomeScreen()
    }
}