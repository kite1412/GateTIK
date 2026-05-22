package kite1412.portaltik.feature.admin.mobile.parking

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph

fun NavGraphBuilder.mobileAdminParkingScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Mobile.Parking.name) {
        MobileAdminParkingScreen()
    }
}