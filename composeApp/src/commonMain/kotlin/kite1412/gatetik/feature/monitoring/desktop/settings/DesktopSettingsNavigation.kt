package kite1412.gatetik.feature.monitoring.desktop.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopSettingsScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Settings.route) {
        DesktopSettingsScreen(
            contentPadding = contentPadding
        )
    }
}