package kite1412.portaltik.feature.monitoring.mobile.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.monitoring.MonitoringGraph

fun NavGraphBuilder.mobileHomeScreen(
    contentPadding: PaddingValues,
    navigateToParking: () -> Unit,
    navigateToCctv: () -> Unit
) {
    composable(MonitoringGraph.Mobile.Home.name) {
        MobileHomeScreen(
            contentPadding = contentPadding,
            navigateToParking = navigateToParking,
            navigateToCctv = navigateToCctv
        )
    }
}