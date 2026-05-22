package kite1412.portaltik.feature.admin.mobile.gate

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph

fun NavGraphBuilder.mobileAdminGateScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Mobile.Gate.name) {
        MobileAdminGateScreen(
            contentPadding = contentPadding
        )
    }
}