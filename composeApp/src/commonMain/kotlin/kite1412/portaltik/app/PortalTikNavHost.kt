package kite1412.portaltik.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kite1412.portaltik.feature.shared.authentication.navigation.AuthenticationRoute
import kite1412.portaltik.feature.shared.authentication.navigation.authenticationScreen
import kite1412.portaltik.feature.student.home.navigation.HomeRoute
import kite1412.portaltik.feature.student.home.navigation.homeScreen
import kite1412.portaltik.model.User

@Composable
fun PortalTikNavHost(
    signedInUser: User?,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = if (signedInUser != null) HomeRoute else AuthenticationRoute,
        modifier = modifier.fillMaxSize()
    ) {
        authenticationScreen()
        homeScreen()
    }
}