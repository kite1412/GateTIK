package kite1412.portaltik.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.LayoutDirection
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController

@Composable
fun consumeSideNavBarWidth(contentPadding: PaddingValues) = PaddingValues(
    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) +
            LocalScaffoldComponentsController.current.getState(ScaffoldComponent.NAV_BAR).size.width,
    bottom = contentPadding.calculateBottomPadding(),
    top = contentPadding.calculateTopPadding(),
    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
)