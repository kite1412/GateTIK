package kite1412.gatetik.feature.monitoring.desktop.parking

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.component.Switch
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald600
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray900
import kite1412.gatetik.designsystem.theme.Red600
import kite1412.gatetik.designsystem.theme.Sky300
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White15
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.designsystem.util.WindowWidthSize
import kite1412.gatetik.designsystem.util.rememberWindowWidthSize
import kite1412.gatetik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.gatetik.model.ParkingQuota
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.network.mock.mockParkingQuota
import kite1412.gatetik.network.mock.mockUser
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DesktopParkingScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopParkingViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val mainParkingQuota by viewModel.mainParkingQuota.collectAsStateWithLifecycle()
    val mainGate by viewModel.mainGate.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopParkingScreen(
            userRole = user.role,
            parkingQuota = mainParkingQuota,
            allowedRadiusMeter = mainGate.data?.allowedRadiusMeter ?: 0,
            contentPadding = contentPadding,
            onThemeToggle = viewModel::updateDarkMode,
            onSaveParkingCapacity = viewModel::updateParkingCapacity,
            onAutoRestrictStudentsChange = viewModel::updateAutoRestrictStudent,
            onAllowedRadiusMeterChange = viewModel::updateAllowedRadiusMeter,
            onRefreshClick = viewModel::refreshParkingQuota,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopParkingScreen(
    userRole: UserRole,
    parkingQuota: LoadState<ParkingQuota?>,
    allowedRadiusMeter: Int,
    contentPadding: PaddingValues,
    onThemeToggle: (Boolean) -> Unit,
    onSaveParkingCapacity: (Int) -> Unit,
    onAutoRestrictStudentsChange: (Boolean) -> Unit,
    onAllowedRadiusMeterChange: (Int) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current

    DesktopLayout(
        title = "Manajemen Parkir",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier(),
        onRefreshClick = onRefreshClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadState(
                state = parkingQuota,
                loading = {
                    CircularProgressIndicator()
                },
                error = {
                    Text(
                        text = "Gagal memuat informasi parkir",
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                success = { parkingQuota ->
                    parkingQuota?.let {
                        val windowWidthSize = rememberWindowWidthSize()
                        var capacity by retain(parkingQuota.totalSlots) {
                            mutableStateOf(parkingQuota.totalSlots.toString())
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = contentPadding
                        ) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Max),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    StudentCapacity(
                                        capacity = capacity,
                                        onCapacityChange = { capacity = it },
                                        onSaveClick = {
                                            runCatching { capacity.toInt() }
                                                .getOrNull()
                                                ?.takeIf { it >= 0 }
                                                ?.let(onSaveParkingCapacity)
                                                ?: run {
                                                    capacity = parkingQuota.totalSlots.toString()
                                                    snackbarHostStateWrapper.showSnackbar("Gagal memperbarui kapasitas parkir")
                                                }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    )
                                    if (windowWidthSize == WindowWidthSize.LARGE) Occupancy(
                                        usedSlots = parkingQuota.usedSlots,
                                        totalSlots = parkingQuota.totalSlots,
                                        modifier = Modifier
                                            .weight(3f)
                                            .fillMaxHeight()
                                    )
                                }
                            }
                            if (windowWidthSize != WindowWidthSize.LARGE) item {
                                Occupancy(
                                    usedSlots = parkingQuota.usedSlots,
                                    totalSlots = parkingQuota.totalSlots
                                )
                            }
                            item {
                                Settings(
                                    autoRestrictStudents = parkingQuota.autoRestrictStudents,
                                    allowedRadiusMeter = allowedRadiusMeter,
                                    onAutoRestrictStudentChange = onAutoRestrictStudentsChange,
                                    onAllowedRadiusMeterChange = onAllowedRadiusMeterChange
                                )
                            }
                            item {
                                Notes()
                            }
                        }
                    } ?: Text(
                        text = "Gagal memuat informasi parkir",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            )
        }
    }
}

@Composable
private fun StudentCapacity(
    capacity: String,
    onCapacityChange: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = LocalContentColor.current.copy(alpha = 0.7f)

    Section(
        title = "Kapasitas Mahasiswa",
        modifier = modifier,
        titleColor = contentColor
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = capacity,
                onValueChange = onCapacityChange,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { onSaveClick(capacity) },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("Simpan")
            }
        }
        Text(
            text = "Kapasitas maksimum untuk parkir mahasiswa",
            style = MaterialTheme.typography.bodySmall,
            color = contentColor
        )
    }
}

@Composable
private fun Occupancy(
    usedSlots: Int,
    totalSlots: Int,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current
) {
    val titleColor = LocalContentColor.current.copy(alpha = 0.7f)
    val trackColor by animateColorAsState(
        targetValue = if (isDarkMode) White15 else Gray900.copy(alpha = 0.1f)
    )
    val usedPercentage = ((usedSlots.toFloat() / totalSlots) * 100).roundToInt()

    Section(
        title = "Parkir Mahasiswa",
        modifier = modifier,
        titleColor = titleColor,
        titleTrailing = {
            Text(
                text = if (totalSlots > 0)
                        "$usedSlots / $totalSlots ($usedPercentage%)"
                    else "0",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (usedPercentage > 100) Red600 else LocalContentColor.current
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { usedSlots.toFloat() / totalSlots },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(CircleShape),
                color = Sky300,
                strokeCap = StrokeCap.Butt,
                gapSize = 0.dp,
                trackColor = trackColor,
                drawStopIndicator = {}
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OccupancyInfo(
                    label = "Total",
                    value = totalSlots.toString()
                )
                OccupancyInfo(
                    label = "Ditempati",
                    value = usedSlots.toString()
                )
                OccupancyInfo(
                    label = "Tersisa",
                    value = (totalSlots - usedSlots).toString()
                )
            }
        }
    }
}

@Composable
private fun RowScope.OccupancyInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun Settings(
    autoRestrictStudents: Boolean,
    allowedRadiusMeter: Int,
    onAutoRestrictStudentChange: (Boolean) -> Unit,
    onAllowedRadiusMeterChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Section(
        title = "Pengaturan",
        modifier = modifier
    ) {
        Card(
            icon = GateTikIcons.settings,
            label = "Pembatasan Otomatis",
            description = "Batasi akses mahasiswa ketika parkir sudah penuh"
        ) {
            Switch(
                checked = autoRestrictStudents,
                onCheckedChange = onAutoRestrictStudentChange
            )
        }
        Card(
            icon = GateTikIcons.locateFixed,
            label = "Jangkauan Radius Akses",
            description = "Atur radius maksimum (dalam meter) untuk mahasiswa dapat mengakses gate"
        ) {
            CapacitySelector(
                steps = listOf(0, 25, 50, 100, 200),
                onSelectedChange = onAllowedRadiusMeterChange,
                selected = allowedRadiusMeter,
                modifier = Modifier
                    .widthIn(min = 160.dp, max = 190.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CapacitySelector(
    steps: List<Int>,
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val density = LocalDensity.current
        val maxWidth = with(density) {
            constraints.maxWidth.toDp()
        }

        Canvas(
            Modifier
                .width(maxWidth)
                .height(8.dp)
                .clip(CircleShape)
        ) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFD64545),
                        Color(0xFFE58A4A),
                        Color(0xFFD6A600),
                        Color(0xFFB4B15D),
                        Color(0xFF4FAF7B)
                    )
                )
            )
        }

        Row(
            modifier = Modifier.width(maxWidth),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            steps.forEach { step ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val selected = selected == step
                    val background by animateColorAsState(
                        targetValue = if (selected) Blue500 else Color.Transparent
                    )
                    val indicatorBackground by animateColorAsState(
                        targetValue = if (selected) White else Color.Transparent
                    )
                    val shape = RoundedCornerShape(8.dp)

                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(indicatorBackground)
                            .border(
                                width = 1.dp,
                                color = White,
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = step.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .background(
                                color = background,
                                shape = shape
                            )
                            .clip(shape)
                            .clickable { onSelectedChange(step) }
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),
                        color = if (selected) White else LocalContentColor.current
                    )
                }
            }
        }
    }
}

@Composable
private fun Notes(modifier: Modifier = Modifier) {
    Section(
        title = "Catatan",
        modifier = modifier
    ) {
        Card(
            icon = GateTikIcons.car,
            label = "Sistem Kuota Parkir Mahasiswa",
            description = "Slot parkir untuk mahasiswa dibatasi sesuai kuota kapasitas yang tersedia. Akses mahasiswa akan diblokir secara otomatis ketika kapasitas penuh.",
            trailing = {},
            iconColor = Blue500,
            verticalAlignment = Alignment.Top
        )
        Card(
            icon = GateTikIcons.people,
            label = "Akses Staff & Admin",
            description = "Staf dan admin memiliki akses parkir tanpa batas serta tidak dikenakan pembatasan berdasarkan kapasitas parkir.",
            trailing = {},
            iconColor = Emerald600,
            verticalAlignment = Alignment.Top
        )
    }
}

@Composable
private fun Card(
    icon: DrawableResource,
    label: String,
    description: String,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconColor: Color = LocalContentColor.current,
    trailing: (@Composable () -> Unit)? = null
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        val contentColor = LocalContentColor.current

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = verticalAlignment,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = label,
                    tint = iconColor
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.6f)
                    )
                }
            }
            trailing?.let { trailing ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    trailing()
                }
            }
        }
    }
}

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = LocalContentColor.current,
    titleTrailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassBox(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = titleColor
                )
                titleTrailing?.invoke()
            }
            content(this)
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopParkingScreenPreview() {
    GateTikTheme(darkMode = isSystemInDarkTheme()) {
        CompositionLocalProvider(
            LocalScaffoldComponentsController provides MockScaffoldComponentController,
            LocalDarkMode provides isSystemInDarkTheme()
        ) {
            Scaffold { p ->
                DesktopParkingScreen(
                    userRole = mockUser.role,
                    parkingQuota = LoadState.Success(mockParkingQuota),
                    allowedRadiusMeter = 50,
                    contentPadding = p,
                    onThemeToggle = {},
                    onSaveParkingCapacity = {},
                    onAutoRestrictStudentsChange = {},
                    onAllowedRadiusMeterChange = {},
                    onRefreshClick = {}
                )
            }
        }
    }
}