package kite1412.portaltik.feature.admin.mobile.cctv

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph

fun NavGraphBuilder.mobileAdminCctvScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Mobile.Cctv.name) {
        MobileAdminCctvScreen()
    }
}