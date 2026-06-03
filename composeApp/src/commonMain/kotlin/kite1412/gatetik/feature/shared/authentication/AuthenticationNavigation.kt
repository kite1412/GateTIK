package kite1412.gatetik.feature.shared.authentication

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.shared.SharedGraph

fun NavGraphBuilder.authenticationScreen(contentPadding: PaddingValues) {
    composable(SharedGraph.AuthenticationRoute.name) {
        AuthenticationScreen(
            contentPadding = contentPadding
        )
    }
}