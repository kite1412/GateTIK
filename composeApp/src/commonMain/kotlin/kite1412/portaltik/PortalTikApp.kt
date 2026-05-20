package kite1412.portaltik

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun PortalTikApp() {
    val viewModel = koinViewModel<PortalTikViewModel>()

    PortalTikTheme {
        PortalTikNavHost(
            signedInUser = viewModel.signedInUser
        )
    }
}