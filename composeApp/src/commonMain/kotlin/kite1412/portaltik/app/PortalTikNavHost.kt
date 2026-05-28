package kite1412.portaltik.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kite1412.portaltik.feature.monitoring.MonitoringGraph
import kite1412.portaltik.feature.monitoring.monitoringGraph
import kite1412.portaltik.feature.shared.SharedGraph
import kite1412.portaltik.feature.shared.sharedGraph
import kite1412.portaltik.feature.student.StudentGraph
import kite1412.portaltik.feature.student.studentGraph
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider

@Composable
fun PortalTikNavHost(
    signedInUser: User?,
    scaffoldPadding: PaddingValues,
    rootDestinationsProvider: RootDestinationsProvider?,
    navigateToRootDestination: (RootDestination) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (signedInUser == null) SharedGraph.route
        else when(signedInUser.role) {
            UserRole.STUDENT -> StudentGraph.route
            else -> MonitoringGraph.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        sharedGraph(
            scaffoldPadding = scaffoldPadding,
            rootDestinationsProvider = rootDestinationsProvider
        )
        monitoringGraph(
            scaffoldPadding = scaffoldPadding,
            navigateToRootDestination = navigateToRootDestination
        )
        studentGraph(scaffoldPadding = scaffoldPadding)
    }
}