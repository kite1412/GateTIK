package kite1412.gatetik.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import kite1412.gatetik.designsystem.component.Destination
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.NavigationScaffold
import kite1412.gatetik.designsystem.extension.linkStyle
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Yellow300
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.compositionlocal.LocalWindowBlurRequester
import kite1412.gatetik.ui.compositionlocal.SnackbarHostStateWrapper
import kite1412.gatetik.ui.navigation.RootDestination
import kite1412.gatetik.ui.navigation.toNavBarDestination
import kite1412.gatetik.ui.util.ScaffoldComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun GateTikApp() {
    val viewModel = koinViewModel<GateTikViewModel>()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsStateWithLifecycle()
    val sessionStatus by viewModel.sessionStatus.collectAsStateWithLifecycle()
    val appLatestVersion = viewModel.appLatestVersion
    val hasUpdate = appLatestVersion?.let(viewModel.versionChecker::hasUpdate) ?: false
    val hasBreakingChanges = appLatestVersion?.let(viewModel.versionChecker::hasBreakingChanges) ?: false
    val scaffoldComponentsController = viewModel.scaffoldComponentsController
    val navController = rememberNavController()
    val user = if (sessionStatus is SessionStatus.SignedIn)
        (sessionStatus as SessionStatus.SignedIn).user else null
    val appState = rememberGateTikAppState(
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
        LocalSnackbarHostStateWrapper provides snackbarHostStateWrapper,
        LocalWindowBlurRequester provides appState.windowBlurRequester
    ) {
        GateTikTheme {
            Scaffold { p ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .radialBackground()
                        .blur(appState.windowBlur),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!hasBreakingChanges) {
                        val showAppUpdateNotification = hasUpdate && viewModel.showAppUpdateNotification
                        AnimatedVisibility(
                            visible = showAppUpdateNotification
                        ) {
                            AppUpdateNotificationBar(
                                latestVersionDownloadUrl = appLatestVersion?.downloadUrl,
                                isDarkMode = LocalDarkMode.current,
                                topPadding = p.calculateTopPadding(),
                                onDismissClick = viewModel::dismissAppUpdateNotification
                            )
                        }
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
                            onDismissNavBar = {
                                scaffoldComponentsController.hideComponent(ScaffoldComponent.NAV_BAR)
                            }
                        ) {
                            Box {
                                this@Column.AnimatedVisibility(
                                    visible = sessionStatus !is SessionStatus.Loading && isFirstLaunch != null,
                                    enter = fadeIn() + slideInHorizontally()
                                ) {
                                    GateTikNavHost(
                                        signedInUser = user,
                                        isFirstLaunch = isFirstLaunch!!,
                                        scaffoldPadding = if (!showAppUpdateNotification) p else PaddingValues(
                                            start = p.calculateLeftPadding(LayoutDirection.Ltr),
                                            end = p.calculateRightPadding(LayoutDirection.Ltr),
                                            bottom = p.calculateBottomPadding()
                                        ),
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
                                    snackbar = { data -> GateTikSnackbar(data) }
                                )
                            }
                        }
                    } else ForceUpdateScreen(
                        latestVersionDownloadUrl = appLatestVersion.downloadUrl,
                        isDarkMode = LocalDarkMode.current
                    )
                }
            }
        }
    }
}

@Composable
private fun AppUpdateNotificationBar(
    latestVersionDownloadUrl: String?,
    isDarkMode: Boolean,
    topPadding: Dp,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Yellow300.copy(alpha = 0.2f))
            .padding(top = topPadding)
            .drawBehind {
                drawLine(
                    color = Yellow500,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height)
                )
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val bodySmall = MaterialTheme.typography.bodySmall

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(GateTikIcons.triangleAlert),
                contentDescription = "update aplikasi",
                modifier = Modifier.size((bodySmall.fontSize.value * 1.5f).dp),
                tint = Yellow500
            )
            Text(
                text = buildAnnotatedString {
                    append("Versi terbaru aplikasi tersedia, ")

                    withLink(
                        link = LinkAnnotation.Url(latestVersionDownloadUrl ?: "")
                    ) {
                        linkStyle(isDarkMode) {
                            append("Update aplikasi")
                        }
                    }
                },
                style = bodySmall,
                color = Yellow500
            )
        }
        Icon(
            painter = painterResource(GateTikIcons.x),
            contentDescription = "sembunyikan",
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = onDismissClick
            ),
            tint = Yellow500
        )
    }
}

@Composable
private fun ForceUpdateScreen(
    latestVersionDownloadUrl: String?,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Icon(
            painter = painterResource(GateTikIcons.triangleAlert),
            contentDescription = "breaking change",
            modifier = Modifier.size(40.dp),
            tint = Red500
        )
        Text(
            text = buildAnnotatedString {
                append("Versi aplikasi sudah tidak didukung, update aplikasi untuk lanjut menggunakan")

                latestVersionDownloadUrl?.let {
                    append(", ")
                    withLink(
                        link = LinkAnnotation.Url(it)
                    ) {
                        linkStyle(isDarkMode) {
                            append("Update aplikasi")
                        }
                    }
                }
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}