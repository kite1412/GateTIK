package kite1412.portaltik.feature.monitoring.desktop.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.ui.component.BorderedHeaderSection

@Composable
fun DesktopLayout(
    title: String,
    userRole: UserRole,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier) {
        BorderedHeaderSection(
            title = title,
            badgeText = userRole.toIdString(),
            onThemeToggle = onThemeToggle
        )
        content()
    }
}