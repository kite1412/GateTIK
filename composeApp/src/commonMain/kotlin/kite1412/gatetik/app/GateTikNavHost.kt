package kite1412.gatetik.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kite1412.gatetik.PlatformType
import kite1412.gatetik.feature.monitoring.MonitoringGraph
import kite1412.gatetik.feature.monitoring.monitoringGraph
import kite1412.gatetik.feature.shared.SharedGraph
import kite1412.gatetik.feature.shared.permissionrequest.permissionRequestScreen
import kite1412.gatetik.feature.shared.sharedGraph
import kite1412.gatetik.feature.student.StudentGraph
import kite1412.gatetik.feature.student.studentGraph
import kite1412.gatetik.getPlatform
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.ui.navigation.RootDestination
import kite1412.gatetik.ui.navigation.RootDestinationsProvider

@Composable
fun GateTikNavHost(
    signedInUser: User?,
    isFirstLaunch: Boolean,
    scaffoldPadding: PaddingValues,
    rootDestinationsProvider: RootDestinationsProvider?,
    navigateToRootDestination: (RootDestination) -> Unit,
    onPermissionRequestsComplete: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (signedInUser == null) SharedGraph.route
        else if (isFirstLaunch && getPlatform().type == PlatformType.MOBILE) SharedGraph.PermissionRequest.name
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
            rootDestinationsProvider = rootDestinationsProvider,
            profileUseDefaultHeader = getPlatform().type == PlatformType.MOBILE
        )
        monitoringGraph(
            scaffoldPadding = scaffoldPadding,
            navigateToRootDestination = navigateToRootDestination
        )
        studentGraph(scaffoldPadding = scaffoldPadding)
        permissionRequestScreen(
            contentPadding = normalContentPadding(scaffoldPadding),
            onPermissionRequestsCompleted = onPermissionRequestsComplete
        )
    }
}