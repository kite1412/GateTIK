package kite1412.portaltik.feature.shared.gateaccess

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.preview.DevicePreviews
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GateAccessScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: GateAccessViewModel = koinViewModel()
) {

}

@Composable
private fun GateAccessScreen(modifier: Modifier = Modifier) {

}

@DevicePreviews
@Composable
private fun GateAccessScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              GateAccessScreen(
                  modifier = Modifier.padding(p)
              )
        }
    }
}