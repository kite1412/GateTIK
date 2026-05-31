package kite1412.portaltik.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import kite1412.portaltik.domain.SessionStatus
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
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsStateWithLifecycle()
    val sessionStatus by viewModel.sessionStatus.collectAsStateWithLifecycle()
    val scaffoldComponentsController = viewModel.scaffoldComponentsController
    val navController = rememberNavController()
    val user = if (sessionStatus is SessionStatus.SignedIn)
        (sessionStatus as SessionStatus.SignedIn).user else null
    val appState = rememberPortalTikAppState(
        navController = navController,
        userRole = user?.role
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
            Scaffold { p ->
                NavigationScaffold(
                    destinations = rootDestinationsProvider
                        ?.rootDestinations
                        ?.map(RootDestination::toNavBarDestination)
                        ?: emptyList(),
                    isDarkMode = LocalDarkMode.current,
                    selectedDestination = appState.currentRootDestination?.toNavBarDestination() ?: Destination.Empty,
                    username = user?.fullName ?: "",
                    userEmail = user?.email ?: "",
                    onDestinationClick = { des ->
                        appState.navigateToRootDestination(des.route)
                    },
                    onSignOutClick = viewModel::onSignOutClick,
                    showNavigationBar = appState.currentRootDestination != null &&
                            viewModel.scaffoldComponentsController
                                .getState(ScaffoldComponent.NAV_BAR)
                                .isVisible,
                    onNavigationBarSizeChange = { size ->
                        scaffoldComponentsController.updateComponentSize(
                            component = ScaffoldComponent.NAV_BAR,
                            size = size
                        )
                    },
                    modifier = Modifier.fillMaxSize().radialBackground()
                ) {
                    Box {
                        AnimatedVisibility(
                            visible = sessionStatus !is SessionStatus.Loading && isFirstLaunch != null,
                            enter = fadeIn() + slideInHorizontally()
                        ) {
                            PortalTikNavHost(
                                signedInUser = user,
                                isFirstLaunch = isFirstLaunch!!,
                                scaffoldPadding = p,
                                rootDestinationsProvider = rootDestinationsProvider,
                                navigateToRootDestination = { rootDestination ->
                                    appState.navigateToRootDestination(rootDestination.route)
                                },
                                onPermissionRequestsComplete = viewModel::completeFirstLaunch,
                                navController = navController
                            )
                        }
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
}