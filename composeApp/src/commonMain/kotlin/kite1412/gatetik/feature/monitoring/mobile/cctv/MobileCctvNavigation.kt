package kite1412.gatetik.feature.monitoring.mobile.cctv

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.mobileCctvScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Mobile.Cctv.name) {
        MobileCctvScreen(contentPadding = contentPadding)
    }
}
