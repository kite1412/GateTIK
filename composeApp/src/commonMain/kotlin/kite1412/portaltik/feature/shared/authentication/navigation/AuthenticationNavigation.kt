package kite1412.portaltik.feature.shared.authentication.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.SharedGraph
import kite1412.portaltik.feature.shared.authentication.AuthenticationScreen

fun NavGraphBuilder.authenticationScreen(contentPadding: PaddingValues) {
    composable(SharedGraph.AuthenticationRoute.name) {
        AuthenticationScreen(
            contentPadding = contentPadding
        )
    }
}