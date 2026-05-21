package kite1412.portaltik.feature.admin.desktop.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopAdminDashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopAdminDashboardViewModel = koinViewModel()
) {
    DesktopAdminDashboardScreen(
        contentPadding = contentPadding
    )
}

@Composable
private fun DesktopAdminDashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Text("Dashboard")
}

@DevicePreviews
@Composable
private fun DashboardScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
            DesktopAdminDashboardScreen(
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier.padding(p)
            )
        }
    }
}