package kite1412.portaltik.feature.admin.mobile.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph
import kite1412.portaltik.feature.admin.mobile.home.MobileAdminHomeScreen

fun NavGraphBuilder.mobileAdminHomeScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Mobile.Home.name) {
        MobileAdminHomeScreen(
            contentPadding = contentPadding
        )
    }
}