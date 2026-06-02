package kite1412.portaltik.feature.monitoring.desktop.usermanagement

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopUserManagementScreen(
    modifier: Modifier = Modifier,
    viewModel: DesktopUserManagementViewModel = koinViewModel()
) {

}

@Composable
private fun DesktopUserManagementScreen(modifier: Modifier = Modifier) {

}

@DevicePreviews
@Composable
private fun DesktopUserManagementScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              DesktopUserManagementScreen(
                  modifier = Modifier.padding(p)
              )
        }
    }
}