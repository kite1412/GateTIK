package kite1412.portaltik.feature.student.gateaccess

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.theme.BlueIndigoGradient
import kite1412.portaltik.designsystem.theme.Emerald700
import kite1412.portaltik.designsystem.theme.Gray900
import kite1412.portaltik.designsystem.theme.Gray950
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red500
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White30
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.network.mock.mockParkingQuota
import kite1412.portaltik.network.mock.mockUser
import kite1412.portaltik.ui.component.GateControlButton
import kite1412.portaltik.ui.component.HeaderSection
import kite1412.portaltik.ui.component.ParkingQuotaCard
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.MockScaffoldComponentController
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.WindowWidthSize
import kite1412.portaltik.ui.util.rememberWindowWidthSize
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GateAccessScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: GateAccessViewModel = koinViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val parkingQuota by viewModel.parkingQuota.collectAsStateWithLifecycle()

    user?.let { user ->
        GateAccessScreen(
            user = user,
            parkingQuota = parkingQuota,
            isLocationPermissionGranted = viewModel.isLocationPermissionGranted,
            isGateLocked = true,
            isDarkMode = LocalDarkMode.current,
            contentPadding = contentPadding,
            onGateOpen = viewModel::openGate,
            modifier = modifier
        )
    }
}

@Composable
private fun GateAccessScreen(
    user: User,
    parkingQuota: LoadState<ParkingQuota?>,
    isLocationPermissionGranted: Boolean,
    isGateLocked: Boolean,
    isDarkMode: Boolean,
    contentPadding: PaddingValues,
    onGateOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navBarHeight = LocalScaffoldComponentsController.current
        .getState(ScaffoldComponent.NAV_BAR)
        .size
        .height
    val windowWidthSize = rememberWindowWidthSize()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        if (isLocationPermissionGranted) LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    bottom = if (windowWidthSize == WindowWidthSize.COMPACT) navBarHeight
                    else 0.dp
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                HeaderSection(
                    userName = user.fullName,
                    userRole = user.role,
                    isDarkMode = isDarkMode
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ParkingQuotaCard(
                        parkingQuota = parkingQuota,
                        isDarkMode = isDarkMode
                    )
                    GateStatusCard(
                        isLocked = isGateLocked,
                        canOpenGate = (if (
                            parkingQuota is LoadState.Success &&
                            parkingQuota.data != null
                        ) parkingQuota.data.usedSlots < parkingQuota.data.totalSlots else false) &&
                            !isGateLocked,
                        isDarkMode = isDarkMode,
                        onGateOpen = onGateOpen
                    )
                }
            }
        } else LocationPermissionWarning(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun LocationPermissionWarning(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}

@Composable
private fun GateStatusCard(
    isLocked: Boolean,
    isDarkMode: Boolean,
    canOpenGate: Boolean,
    onGateOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val baseColor by animateColorAsState(
        targetValue = if (isLocked) Red500 else Emerald700
    )
    val blurColor by infiniteTransition.animateColor(
        initialValue = baseColor.copy(alpha = 0.3f),
        targetValue = baseColor.copy(alpha = 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val radialColor by infiniteTransition.animateColor(
        initialValue = baseColor.copy(alpha = 0.1f),
        targetValue = baseColor.copy(alpha = 0.05f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    GlassBox(
        modifier = modifier
            .heightIn(max = 400.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        isDarkMode = isDarkMode
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
                                baseColor,
                                baseColor.copy(alpha = 0.6f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        if (isLocked) PortalTikIcons.lock
                        else PortalTikIcons.lockOpen
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
            if (isLocked) Text(
                text = "Gate dikunci, hubungi admin untuk membuka gate.",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(vertical = 24.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontStyle = FontStyle.Italic
            )
            GateControlButton(
                isOpen = true,
                actionEnabled = canOpenGate,
                onClick = onGateOpen,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                backgroundBrush = Brush.linearGradient(BlueIndigoGradient),
                disabledBackgroundBrush = SolidColor(
                    if (!isDarkMode) Gray900.copy(alpha = 0.3f)
                    else Gray950.copy(alpha = 0.2f)
                ),
                color = White,
                disabledColor = if (!isDarkMode) White60 else White30,
                contentPadding = PaddingValues(
                    vertical = 16.dp
                )
            )
        }
    }
}

@DevicePreviews
@Composable
private fun GateAccessScreenPreview() {
    val darkMode = isSystemInDarkTheme()

    PortalTikTheme(darkMode = darkMode) {
        CompositionLocalProvider(
            LocalScaffoldComponentsController provides MockScaffoldComponentController
        ) {
            Scaffold { p ->
                GateAccessScreen(
                    user = mockUser.copy(
                        role = UserRole.STUDENT
                    ),
                    isLocationPermissionGranted = true,
                    isGateLocked = false,
                    parkingQuota = LoadState.Success(mockParkingQuota),
                    isDarkMode = darkMode,
                    contentPadding = p,
                    onGateOpen = {},
                    modifier = Modifier.padding(p)
                )
            }
        }
    }
}