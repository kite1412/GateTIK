package kite1412.portaltik.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.navOptions
import kite1412.portaltik.DeviceType
import kite1412.portaltik.feature.admin.AdminGraph
import kite1412.portaltik.feature.student.StudentGraph
import kite1412.portaltik.getDeviceType
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.RootDestinationsProvider

@Composable
fun rememberPortalTikAppState(navController: NavController, userRole: UserRole?) =
    remember(navController, userRole) {
        PortalTikAppState(
            navController = navController,
            userRole = userRole
        )
    }

class PortalTikAppState(
    private val navController: NavController,
    private val userRole: UserRole?
) {
    private val currentNavDestination: NavDestination?
        @Composable get() {
            val currentBackStackEntry = navController.currentBackStackEntryFlow
                .collectAsState(null)

            return currentBackStackEntry.value?.destination
        }

    private var _currentRootDestination: RootDestination? = null

    val currentRootDestination: RootDestination?
        @Composable get() {
            val rootDestinations = getRootDestinationsProvider()?.rootDestinations
                ?: return null

            return rootDestinations.firstOrNull { destination ->
                val navDes = currentNavDestination?.route
                navDes == destination.route
            }
                ?.also {
                    _currentRootDestination = it
                }
        }

    fun navigateToRootDestination(route: String) {
        if (_currentRootDestination?.route == route) return
        val navOptions = navOptions {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            restoreState = true
        }

        navController.navigate(route, navOptions)
    }

    fun getRootDestinationsProvider(): RootDestinationsProvider? =
        userRole?.let { role ->
            val isDesktop = getDeviceType() == DeviceType.DESKTOP
            when (role) {
                UserRole.ADMIN -> if (isDesktop) AdminGraph.Desktop else AdminGraph.Mobile
                UserRole.STAFF -> TODO()
                UserRole.STUDENT -> StudentGraph
            }
        }
}
