package kite1412.portaltik.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun PortalTikApp() {
    val viewModel = koinViewModel<PortalTikViewModel>()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalDarkMode provides (isDarkMode ?: isSystemInDarkTheme())
    ) {
        PortalTikTheme {
            PortalTikNavHost(
                signedInUser = viewModel.signedInUser
            )
        }
    }
}