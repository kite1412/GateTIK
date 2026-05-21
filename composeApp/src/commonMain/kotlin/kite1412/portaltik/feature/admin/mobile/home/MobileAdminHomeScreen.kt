package kite1412.portaltik.feature.admin.mobile.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileAdminHomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileAdminHomeViewModel = koinViewModel()
) {
    MobileAdminHomeScreen(contentPadding = contentPadding)
}

@Composable
private fun MobileAdminHomeScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Text("Admin Home")
}

@DevicePreviews
@Composable
private fun MobileAdminHomeScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              MobileAdminHomeScreen(
                  contentPadding = p,
                  modifier = Modifier.padding(p)
              )
        }
    }
}