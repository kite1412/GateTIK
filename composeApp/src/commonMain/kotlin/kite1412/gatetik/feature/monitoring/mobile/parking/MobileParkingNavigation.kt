package kite1412.gatetik.feature.monitoring.mobile.parking

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.mobileParkingScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Mobile.Parking.name) {
        MobileParkingScreen(contentPadding = contentPadding)
    }
}
