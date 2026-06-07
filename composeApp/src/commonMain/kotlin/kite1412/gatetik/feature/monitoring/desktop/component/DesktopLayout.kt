package kite1412.gatetik.feature.monitoring.desktop.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.ui.component.BorderedHeaderSection
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.util.ScaffoldComponent
import org.jetbrains.compose.resources.painterResource

@Composable
fun DesktopLayout(
    title: String,
    userRole: UserRole,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onRefreshClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val scaffoldComponentsController = LocalScaffoldComponentsController.current

    Column(modifier) {
        BorderedHeaderSection(
            title = title,
            badgeText = userRole.toIdString(),
            onThemeToggle = onThemeToggle,
            leading = {
                AnimatedVisibility(
                    visible = !scaffoldComponentsController
                        .getState(ScaffoldComponent.NAV_BAR).isVisible
                ) {
                    GlassBox(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                scaffoldComponentsController.showComponent(ScaffoldComponent.NAV_BAR)
                            },
                        contentPadding = PaddingValues(8.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.chevronRight),
                            contentDescription = "tutup",
                            modifier = Modifier.size(16.dp),
                            tint = LocalContentColor.current
                        )
                    }
                }
            },
            actions = if (onRefreshClick != null) {
                {
                    val iconSize = 16.dp

                    GlassBox(
                        modifier = Modifier
                            .size(iconSize * 2)
                            .clip(CircleShape)
                            .clickable(onClick = onRefreshClick),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.rotate),
                            contentDescription = "refresh",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(iconSize),
                            tint = LocalContentColor.current
                        )
                    }
                }
            } else null
        )
        content()
    }
}