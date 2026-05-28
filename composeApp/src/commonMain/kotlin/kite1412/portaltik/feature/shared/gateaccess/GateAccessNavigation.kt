package kite1412.portaltik.feature.shared.gateaccess

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.shared.SharedGraph

fun NavGraphBuilder.gateAccessScreen(contentPadding: PaddingValues) {
    composable(SharedGraph.GateAccessRoute.name) {
        GateAccessScreen(
            contentPadding = contentPadding
        )
    }
}