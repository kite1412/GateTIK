package kite1412.gatetik.feature.monitoring.desktop.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.feature.shared.profile.ProfileScreen
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopProfileScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopProfileViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopLayout(
            title = "Profil",
            userRole = user.role,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier.desktopBaseModifier()
        ) {
            ProfileScreen(
                useDefaultHeader = false,
                contentPadding = contentPadding,
                userLoadState = LoadState.Success(user),
                onDarkModeToggle = {},
                onLogout = viewModel::logout,
                onSaveChanges = viewModel::updateProfile,
                onResetPassword = viewModel::resetPassword,
                consumeNavBarSize = false,
                applyContentPaddingOnScrollableContainer = true
            )
        }
    }
}