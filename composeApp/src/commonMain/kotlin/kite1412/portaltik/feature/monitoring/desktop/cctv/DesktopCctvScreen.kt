package kite1412.portaltik.feature.monitoring.desktop.cctv

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.portaltik.feature.monitoring.desktop.component.LiveCameraSection
import kite1412.portaltik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopCctvViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()

    user?.let { user ->
        DesktopLayout(
            title = "CCTV Monitoring",
            userRole = user.role,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier.desktopBaseModifier()
        ) {
            LiveCameraSection(
                cameraName = "CAM-01 · Gerbang Utama",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopCctvScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              DesktopCctvScreen(
                  contentPadding = p
              )
        }
    }
}