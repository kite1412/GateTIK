package kite1412.portaltik.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kite1412.portaltik.feature.admin.AdminGraph
import kite1412.portaltik.feature.admin.adminGraph
import kite1412.portaltik.feature.shared.SharedGraph
import kite1412.portaltik.feature.shared.sharedGraph
import kite1412.portaltik.feature.student.StudentGraph
import kite1412.portaltik.feature.student.studentGraph
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole

@Composable
fun PortalTikNavHost(
    signedInUser: User?,
    scaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (signedInUser == null) SharedGraph.route
        else when(signedInUser.role) {
            UserRole.ADMIN -> AdminGraph.route
            UserRole.STAFF -> SharedGraph.route // TODO change later
            UserRole.STUDENT -> StudentGraph.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        sharedGraph(scaffoldPadding = scaffoldPadding)
        adminGraph(scaffoldPadding = scaffoldPadding)
        studentGraph(scaffoldPadding = scaffoldPadding)
    }
}