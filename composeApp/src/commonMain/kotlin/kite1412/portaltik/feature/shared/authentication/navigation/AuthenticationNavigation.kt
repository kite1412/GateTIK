package kite1412.portaltik.feature.shared.authentication.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.authentication.AuthenticationScreen
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticationRoute

fun NavGraphBuilder.authenticationScreen(contentPadding: PaddingValues) {
    composable<AuthenticationRoute> {
        AuthenticationScreen(
            contentPadding = contentPadding
        )
    }
}