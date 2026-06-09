package kite1412.gatetik.feature.monitoring.desktop.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopProfileScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Profile.route) {
        DesktopProfileScreen(
            contentPadding = contentPadding
        )
    }
}