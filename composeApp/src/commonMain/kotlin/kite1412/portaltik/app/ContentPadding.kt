package kite1412.portaltik.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun smallContentPadding(scaffoldPadding: PaddingValues? = null) = getContentPadding(
    padding = 16.dp,
    scaffoldPadding = scaffoldPadding
)

fun normalContentPadding(scaffoldPadding: PaddingValues? = null) = getContentPadding(
    padding = 24.dp,
    scaffoldPadding
)

fun largeContentPadding(scaffoldPadding: PaddingValues? = null) = getContentPadding(
    padding = 32.dp,
    scaffoldPadding = scaffoldPadding
)

private fun getContentPadding(
    padding: Dp,
    scaffoldPadding: PaddingValues? = null
) = with(padding) {
    PaddingValues(
        start = this + (scaffoldPadding?.calculateLeftPadding(LayoutDirection.Ltr) ?: 0.dp),
        end = this + (scaffoldPadding?.calculateEndPadding(LayoutDirection.Ltr) ?: 0.dp),
        top = this + (scaffoldPadding?.calculateTopPadding() ?: 0.dp),
        bottom = this + (scaffoldPadding?.calculateBottomPadding() ?: 0.dp)
    )
}