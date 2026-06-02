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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.LocationState
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.extension.linkStyle
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
import kite1412.portaltik.designsystem.util.WindowWidthSize
import kite1412.portaltik.designsystem.util.rememberWindowWidthSize
import kite1412.portaltik.model.ParkingQuota
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.network.mock.mockParkingQuota
import kite1412.portaltik.network.mock.mockUser
import kite1412.portaltik.rememberLocationPermissionRequester
import kite1412.portaltik.ui.component.GateControlButton
import kite1412.portaltik.ui.component.HeaderSection
import kite1412.portaltik.ui.component.ParkingQuotaCard
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.MockScaffoldComponentController
import kite1412.portaltik.ui.util.ScaffoldComponent
import kite1412.portaltik.ui.util.UiEvent
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
    val locationState by viewModel.locationState.collectAsStateWithLifecycle()
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current

    user?.let { user ->
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                if (event is UiEvent.ShowSnackbar) snackbarHostStateWrapper.showSnackbar(event.message)
            }
        }
        GateAccessScreen(
            user = user,
            parkingQuota = parkingQuota,
            locationState = locationState,
            isLocationPermissionGranted = viewModel.isLocationPermissionGranted,
            isGateLocked = false,
            isActionDelayed = viewModel.delayAction,
            isDarkMode = LocalDarkMode.current,
            contentPadding = contentPadding,
            onGateOpen = viewModel::openGate,
            onLocationPermissionChange = viewModel::updateIsLocationPermissionGranted,
            modifier = modifier
        )
    }
}

@Composable
private fun GateAccessScreen(
    user: User,
    parkingQuota: LoadState<ParkingQuota?>,
    locationState: LocationState,
    isLocationPermissionGranted: Boolean,
    isGateLocked: Boolean,
    isActionDelayed: Boolean,
    isDarkMode: Boolean,
    contentPadding: PaddingValues,
    onGateOpen: () -> Unit,
    onLocationPermissionChange: (granted: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBarHeight = LocalScaffoldComponentsController.current
        .getState(ScaffoldComponent.NAV_BAR)
        .size
        .height
    val windowWidthSize = rememberWindowWidthSize()
    val locationPermissionRequester = rememberLocationPermissionRequester(onLocationPermissionChange)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        if (isLocationPermissionGranted && locationState is LocationState.Available) LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
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
                            !isGateLocked &&
                            !isActionDelayed,
                        isDarkMode = isDarkMode,
                        onGateOpen = onGateOpen
                    )
                }
            }
        } else if (!isLocationPermissionGranted || locationState is LocationState.Unavailable) LocationWarning(
            isDarkMode = isDarkMode,
            isLocationPermissionGranted = isLocationPermissionGranted,
            onRequestPermissionClick = locationPermissionRequester,
            modifier = Modifier
                .padding(
                    bottom = if (windowWidthSize == WindowWidthSize.COMPACT) navBarHeight
                    else 0.dp
                )
                .align(Alignment.Center)
        ) else Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val color = MaterialTheme.colorScheme.onBackground

            CircularProgressIndicator(color = color)
            Text(
                text = "Memuat informasi lokasi",
                style = MaterialTheme.typography.labelMedium,
                fontStyle = FontStyle.Italic,
                color = color
            )
        }
    }
}

@Composable
private fun LocationWarning(
    isDarkMode: Boolean,
    isLocationPermissionGranted: Boolean,
    onRequestPermissionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PulsingIcon(
            basePulseColor = Red500.copy(alpha = 0.3f),
            painter = painterResource(PortalTikIcons.locationMark)
        )
        Text(
            text = buildAnnotatedString {
                if (!isLocationPermissionGranted) {
                    append("Aktifkan izin lokasi untuk dapat membuka gate, ")
                    withLink(
                        link = LinkAnnotation.Clickable(
                            tag = "request_permission",
                            linkInteractionListener = {
                                onRequestPermissionClick()
                            }
                        )
                    ) {
                        linkStyle(isDarkMode) {
                            append("Izinkan")
                        }
                    }
                } else {
                    append("Aktifkan berbagi lokasi untuk dapat membuka gate")
                }
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
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
    val baseColor by animateColorAsState(
        targetValue = if (isLocked) Red500 else Emerald700
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
            PulsingIcon(
                basePulseColor = baseColor,
                painter = painterResource(
                    if (isLocked) PortalTikIcons.lock
                    else PortalTikIcons.lockOpen
                )
            )
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

@Composable
private fun PulsingIcon(
    basePulseColor: Color,
    painter: Painter,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val innerBlurColor by infiniteTransition.animateColor(
        initialValue = basePulseColor.copy(alpha = 0.3f),
        targetValue = basePulseColor.copy(alpha = 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val outerBlurColor by infiniteTransition.animateColor(
        initialValue = basePulseColor.copy(alpha = 0.1f),
        targetValue = basePulseColor.copy(alpha = 0.05f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier,
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
                    color = innerBlurColor,
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
                    color = outerBlurColor,
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
                            basePulseColor,
                            basePulseColor.copy(alpha = 0.6f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = White
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
                    locationState = LocationState.Unavailable,
                    isLocationPermissionGranted = false,
                    isGateLocked = false,
                    isActionDelayed = false,
                    parkingQuota = LoadState.Success(mockParkingQuota),
                    isDarkMode = darkMode,
                    contentPadding = p,
                    onGateOpen = {},
                    onLocationPermissionChange = {},
                    modifier = Modifier.padding(p)
                )
            }
        }
    }
}