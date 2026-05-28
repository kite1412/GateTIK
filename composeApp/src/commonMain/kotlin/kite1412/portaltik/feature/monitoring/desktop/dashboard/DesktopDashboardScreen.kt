package kite1412.portaltik.feature.monitoring.desktop.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.portaltik.CctvPlayer
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopDashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopDashboardViewModel = koinViewModel()
) {
    DesktopDashboardScreen(
        contentPadding = contentPadding
    )
}

@Composable
private fun DesktopDashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    CctvPlayer(
        modifier = modifier
            .aspectRatio(16f/9f)
            .fillMaxSize()
            .padding(contentPadding)
    ) {}
}

@DevicePreviews
@Composable
private fun DesktopDashboardScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
            DesktopDashboardScreen(
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier.padding(p)
            )
        }
    }
}