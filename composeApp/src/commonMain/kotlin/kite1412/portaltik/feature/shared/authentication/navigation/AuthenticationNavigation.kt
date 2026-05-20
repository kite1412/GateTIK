package kite1412.portaltik.feature.shared.authentication.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.authentication.AuthenticationScreen
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticationRoute

fun NavGraphBuilder.authenticationScreen() {
    composable<AuthenticationRoute> {
        AuthenticationScreen()
    }
}