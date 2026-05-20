package kite1412.portaltik.feature.student.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              HomeScreen(
                  modifier = Modifier.padding(p)
              )
        }
    }
}