package kite1412.portaltik.feature.admin.mobile.gate

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.GradientTextButton
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.component.OutlinedTextButton
import kite1412.portaltik.designsystem.theme.Blue200_50
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Slate900_95
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White55
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.ScaffoldComponentState
import kite1412.portaltik.ui.util.ScaffoldComponentsController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileAdminGateScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileAdminGateViewModel = koinViewModel()
) {
    MobileAdminGateScreen(
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileAdminGateScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scaffoldComponentsController = LocalScaffoldComponentsController.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(
            bottom = scaffoldComponentsController.getState(ScaffoldComponent.NAV_BAR).size.height
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            GateControlHeader(
                title = "Kontrol Gerbang",
                subtitle = "Pintu Masuk Utama · Gedung A"
            )
        }

        item {
            GateStatusCard()
        }

        item {
            StatusGrid()
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GradientTextButton(
                    text = "BUKA GATE",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    leading = {
                        Icon(
                            painter = painterResource(PortalTikIcons.doorOpen),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = White
                        )
                    }
                )

                OutlinedTextButton(
                    text = "Simulasi Offline",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun GateControlHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private val GateLockGlow = Color(0xFF2B57FF)

@Composable
private fun GateStatusCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val blurColor by infiniteTransition.animateColor(
        initialValue = GateLockGlow.copy(alpha = 0.3f),
        targetValue = GateLockGlow.copy(alpha = 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val radialColor by infiniteTransition.animateColor(
        initialValue = GateLockGlow.copy(alpha = 0.1f),
        targetValue = GateLockGlow.copy(alpha = 0.05f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    GlassBox(
        modifier = modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .blur(
                        radius = 10.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    .background(
                        color = blurColor,
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .blur(
                        radius = 5.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
                    .background(
                        color = radialColor,
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GateLockGlow,
                                GateLockGlow.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(PortalTikIcons.lock),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
        }
    }
}

@Composable
private fun StatusGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusGridItem(
                label = "STATUS",
                value = "CLOSED",
                modifier = Modifier.weight(1f)
            )
            StatusGridItem(
                label = "KONEKSI",
                value = "Online",
                valueColor = Emerald500,
                icon = painterResource(PortalTikIcons.wifi),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusGridItem(
                label = "TERAKHIR DIBUKA",
                value = "2 menit yang lalu",
                modifier = Modifier.weight(1f)
            )
            StatusGridItem(
                label = "PARKIR",
                value = "72/100",
                valueColor = Emerald500,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatusGridItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: Painter? = null
) {
    val isDarkMode = LocalDarkMode.current
    val containerColor = if (isDarkMode) Slate900_95 else White55
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .run {
                if (!isDarkMode) border(
                    width = 1.dp,
                    color = Blue200_50,
                    shape = shape
                ) else this
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = valueColor
                )
            }
            Text(
                text = value,
                style = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = valueColor
            )
        }
    }
}

@DevicePreviews
@Composable
private fun MobileAdminGateScreenPreview() {
    PortalTikTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold { p ->
            CompositionLocalProvider(
                LocalScaffoldComponentsController provides object : ScaffoldComponentsController {
                    override fun getState(component: ScaffoldComponent): ScaffoldComponentState =
                        ScaffoldComponentState()
                    override fun showComponent(component: ScaffoldComponent) {}
                    override fun hideComponent(component: ScaffoldComponent) {}
                    override fun updateComponentSize(
                        component: ScaffoldComponent,
                        size: DpSize
                    ) {}
                }
            ) {
                MobileAdminGateScreen(
                    contentPadding = p
                )
            }
        }
    }
}