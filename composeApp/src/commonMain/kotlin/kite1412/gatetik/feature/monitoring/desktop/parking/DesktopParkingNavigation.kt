package kite1412.gatetik.feature.monitoring.desktop.parking

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.desktopParkingScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Desktop.Parking.name) {
        DesktopParkingScreen(
            contentPadding = contentPadding
        )
    }
}