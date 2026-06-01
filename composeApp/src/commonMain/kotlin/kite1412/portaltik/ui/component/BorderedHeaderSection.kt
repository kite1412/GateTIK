package kite1412.portaltik.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.extension.radialBackground
import kite1412.portaltik.designsystem.theme.Blue200
import kite1412.portaltik.designsystem.theme.Blue900
import kite1412.portaltik.designsystem.theme.Gray200
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Slate900
import kite1412.portaltik.designsystem.theme.White30
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

@Composable
fun BorderedHeaderSection(
    title: String,
    badgeText: String,
    onThemeToggle: (darkMode: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current,
    leading: (@Composable RowScope.() -> Unit)? = null
) {
    val background by animateColorAsState(
        targetValue = if (isDarkMode) Slate900.copy(alpha = 0.2f) else White30
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(background)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                leading?.invoke(this)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val isDarkMode = LocalDarkMode.current
                ThemeToggle(
                    onToggle = { onThemeToggle(!isDarkMode) },
                    isDarkMode = isDarkMode,
                    iconSize = 16.dp,
                    clip = RoundedCornerShape(12.dp)
                )

                Badge(
                    text = badgeText.uppercase(),
                    containerColor = if (isDarkMode) Blue900.copy(alpha = 0.4f) else Blue200.copy(alpha = 0.4f),
                    contentColor = if (isDarkMode) Blue200 else Blue900
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            thickness = 1.dp,
            color = if (isDarkMode) Gray200.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun BorderedHeaderSectionSectionPreview() {
    PortalTikTheme {
        Scaffold { p ->
              Box(
                  modifier = Modifier
                      .fillMaxSize()
                      .radialBackground()
              ) {
                  Text("Text")
                  BorderedHeaderSection(
                      title = "Dashboard",
                      badgeText = "ADMIN",
                      onThemeToggle = {},
                      modifier = Modifier.padding(p)
                  )
              }
        }
    }
}