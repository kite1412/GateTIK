package kite1412.gatetik.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.navOptions
import kite1412.gatetik.PlatformType
import kite1412.gatetik.feature.monitoring.MonitoringGraph
import kite1412.gatetik.feature.student.StudentGraph
import kite1412.gatetik.getPlatform
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.ui.compositionlocal.WindowBlurRequester
import kite1412.gatetik.ui.navigation.RootDestination
import kite1412.gatetik.ui.navigation.RootDestinationsProvider

@Composable
fun rememberGateTikAppState(navController: NavController, userRole: UserRole?) =
    remember(navController, userRole) {
        GateTikAppState(
            navController = navController,
            userRole = userRole
        )
    }

class GateTikAppState(
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
    var windowBlur by mutableStateOf(0.dp)
        private set

    val windowBlurRequester = object : WindowBlurRequester {
        override fun applyWindowBlur() {
            windowBlur = 4.dp
        }

        override fun removeWindowBlue() {
            windowBlur = 0.dp
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
            val isDesktop = getPlatform().type == PlatformType.DESKTOP

            when (role) {
                UserRole.ADMIN -> if (isDesktop) MonitoringGraph.Desktop else MonitoringGraph.Mobile
                UserRole.STAFF -> if (isDesktop) MonitoringGraph.Desktop else MonitoringGraph.Mobile
                UserRole.STUDENT -> StudentGraph
            }
        }
}
