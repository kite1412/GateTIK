package kite1412.portaltik.feature.admin.mobile.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph

fun NavGraphBuilder.mobileAdminProfileScreen(contentPadding: PaddingValues) {
    composable(AdminGraph.Mobile.Profile.name) {
        MobileAdminProfileScreen(contentPadding = contentPadding)
    }
}