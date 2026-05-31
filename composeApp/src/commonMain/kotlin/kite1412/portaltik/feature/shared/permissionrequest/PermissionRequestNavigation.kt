package kite1412.portaltik.feature.shared.permissionrequest

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.SharedGraph

fun NavGraphBuilder.permissionRequestScreen(
    contentPadding: PaddingValues,
    onAllPermissionsGranted: () -> Unit
) {
    composable(SharedGraph.PermissionRequest.name) {
        PermissionRequestScreen(
            contentPadding = contentPadding,
            onAllPermissionsGranted = onAllPermissionsGranted
        )
    }
}