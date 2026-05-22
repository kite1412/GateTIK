package kite1412.portaltik.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import kite1412.portaltik.designsystem.component.Destination
import kite1412.portaltik.designsystem.component.NavigationScaffold
import kite1412.portaltik.designsystem.extension.radialBackground
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.navigation.RootDestination
import kite1412.portaltik.ui.navigation.toNavBarDestination
import kite1412.portaltik.ui.util.ScaffoldComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun PortalTikApp() {
    val viewModel = koinViewModel<PortalTikViewModel>()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val signedInUser by viewModel.signedInUser.collectAsStateWithLifecycle()
    val scaffoldComponentsController = viewModel.scaffoldComponentsController
    val navController = rememberNavController()
    val appState = rememberPortalTikAppState(
        navController = navController,
        userRole = signedInUser?.role
    )
    val rootDestinationsProvider = appState.getRootDestinationsProvider()

    CompositionLocalProvider(
        LocalDarkMode provides (isDarkMode ?: isSystemInDarkTheme()),
        LocalScaffoldComponentsController provides scaffoldComponentsController
    ) {
        PortalTikTheme {
            NavigationScaffold(
                destinations = rootDestinationsProvider
                    ?.rootDestinations
                    ?.map(RootDestination::toNavBarDestination)
                    ?: emptyList(),
                isDarkTheme = LocalDarkMode.current,
                selectedDestination = appState.currentRootDestination?.toNavBarDestination() ?: Destination.Empty,
                username = signedInUser?.fullName ?: "",
                userEmail = signedInUser?.email ?: "",
                onDestinationClick = { des ->
                    appState.navigateToRootDestination(des.route)
                },
                onLogoutClick = {},
                showNavigationBar = appState.currentRootDestination != null &&
                    viewModel.scaffoldComponentsController
                        .getState(ScaffoldComponent.NAV_BAR)
                        .isVisible,
                onNavigationBarSizeChange = { size ->
                    scaffoldComponentsController.updateComponentSize(
                        component = ScaffoldComponent.NAV_BAR,
                        size = size
                    )
                }
            ) { p ->
                PortalTikNavHost(
                    signedInUser = signedInUser,
                    scaffoldPadding = p,
                    modifier = Modifier.run {
                        if (!LocalDarkMode.current) radialBackground()
                        else this
                    },
                    navController = navController
                )
            }
        }
    }
}