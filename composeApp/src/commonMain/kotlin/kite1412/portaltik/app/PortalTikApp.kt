package kite1412.portaltik.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import kite1412.portaltik.designsystem.component.Destination
import kite1412.portaltik.designsystem.component.NavigationScaffold
import kite1412.portaltik.designsystem.extension.radialBackground
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.portaltik.ui.compositionlocal.SnackbarHostStateWrapper
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
    val snackbarHostStateWrapper = remember {
        SnackbarHostStateWrapper(
            coroutineScope = viewModel.viewModelScope
        )
    }

    CompositionLocalProvider(
        LocalDarkMode provides (isDarkMode ?: isSystemInDarkTheme()),
        LocalScaffoldComponentsController provides scaffoldComponentsController,
        LocalSnackbarHostStateWrapper provides snackbarHostStateWrapper
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
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PortalTikNavHost(
                        signedInUser = signedInUser,
                        scaffoldPadding = p,
                        navigateToRootDestination = { rootDestination ->
                            appState.navigateToRootDestination(rootDestination.route)
                        },
                        modifier = Modifier.radialBackground(),
                        navController = navController
                    )
                    SnackbarHost(
                        hostState = snackbarHostStateWrapper.snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(p),
                        snackbar = { data -> PortalTikSnackbar(data) }
                    )
                }
            }
        }
    }
}