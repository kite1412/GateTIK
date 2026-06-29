package kite1412.gatetik.feature.monitoring.mobile.intercom

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.gatetik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.mobileIntercomScreen(contentPadding: PaddingValues) {
    composable(MonitoringGraph.Mobile.Intercom.route) {
        MobileIntercomScreen(
            contentPadding = contentPadding
        )
    }
}