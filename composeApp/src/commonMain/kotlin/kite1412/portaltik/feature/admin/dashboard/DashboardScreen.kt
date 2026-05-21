package kite1412.portaltik.feature.admin.dashboard

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
fun DashboardScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {
    DashboardScreen(
        contentPadding = contentPadding
    )
}

@Composable
private fun DashboardScreen(
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
            DashboardScreen(
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier.padding(p)
            )
        }
    }
}