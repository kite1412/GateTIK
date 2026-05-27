package kite1412.portaltik.feature.admin.mobile.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.admin.AdminGraph

fun NavGraphBuilder.mobileAdminHomeScreen(
    contentPadding: PaddingValues,
    navigateToParking: () -> Unit,
    navigateToCctv: () -> Unit
) {
    composable(AdminGraph.Mobile.Home.name) {
        MobileAdminHomeScreen(
            contentPadding = contentPadding,
            navigateToParking = navigateToParking,
            navigateToCctv = navigateToCctv
        )
    }
}