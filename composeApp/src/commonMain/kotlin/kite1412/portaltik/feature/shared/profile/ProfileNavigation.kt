package kite1412.portaltik.feature.shared.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.SharedGraph

fun NavGraphBuilder.profileScreen(contentPadding: PaddingValues) {
    composable(SharedGraph.ProfileRoute.name) {
        ProfileScreen(contentPadding = contentPadding)
    }
}