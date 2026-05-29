package kite1412.portaltik.feature.student.gateaccess

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kite1412.portaltik.feature.student.StudentGraph

fun NavGraphBuilder.gateAccessScreen(contentPadding: PaddingValues) {
    composable(StudentGraph.GateAccess.name) {
        GateAccessScreen(
            contentPadding = contentPadding
        )
    }
}