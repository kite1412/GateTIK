package kite1412.gatetik.feature.monitoring.desktop.cctv

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.GradientTextButton
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Blue700
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.domain.model.CctvCreate
import kite1412.gatetik.domain.model.CctvUpdate
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.AddCctvDialog
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvActionBar
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvGridItem
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvStatsCard
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.DeleteCctvDialog
import kite1412.gatetik.feature.monitoring.desktop.ui.component.CctvWindow
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.compositionlocal.LocalWindowBlurRequester
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.util.toLocalDateString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopCctvViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val cctvs = viewModel.cctvs
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val gridColumns by viewModel.gridColumns.collectAsStateWithLifecycle()
    val windowBlurRequester = LocalWindowBlurRequester.current

    var showAddDialog by retain { mutableStateOf(false) }
    var tempSearchQuery by retain(searchQuery) { mutableStateOf(searchQuery) }
    var showDeleteDialog by retain { mutableStateOf<Cctv?>(null) }
    var selectedCameraForEdit by retain { mutableStateOf<Cctv?>(null) }

    val openAddDialog = {
        if (selectedTab != CctvTab.MANAGE) viewModel.updateSelectedTab(CctvTab.MANAGE)
        showAddDialog = true
        windowBlurRequester.applyWindowBlur()
    }
    val openEditDialog = { camera: Cctv ->
        selectedCameraForEdit = camera
        if (selectedTab != CctvTab.MANAGE) viewModel.updateSelectedTab(CctvTab.MANAGE)
        showAddDialog = true
        windowBlurRequester.applyWindowBlur()
    }
    val openDeleteDialog = { camera: Cctv ->
        showDeleteDialog = camera
        if (selectedTab != CctvTab.MANAGE) viewModel.updateSelectedTab(CctvTab.MANAGE)
        windowBlurRequester.applyWindowBlur()
    }
    val dismissDialogs = {
        showAddDialog = false
        selectedCameraForEdit = null
        showDeleteDialog = null
        windowBlurRequester.removeWindowBlue()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopLayout(
            title = "Monitoring CCTV",
            userRole = user.role,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier.desktopBaseModifier(),
            onRefreshClick = viewModel::refreshCctvs
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                        end = contentPadding.calculateRightPadding(LayoutDirection.Ltr),
                        top = contentPadding.calculateTopPadding()
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CctvHeader(
                    cameraCount = cctvs.data?.size,
                    onAddClick = openAddDialog
                )

                CctvStats(
                    cameraCount = cctvs.data?.size
                )

                CctvActionBar(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::updateSelectedTab,
                    searchQuery = tempSearchQuery,
                    onSearchQueryChange = { tempSearchQuery = it },
                    gridColumns = gridColumns,
                    onGridColumnsChange = viewModel::updateGridColumns,
                    modifier = Modifier.onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                            viewModel.updateSearchQuery(tempSearchQuery)
                            true
                        } else false
                    }
                )

                val filteredCameras = cctvs.data?.filter {
                    it.cameraName.contains(searchQuery, ignoreCase = true) ||
                            it.path.contains(searchQuery, ignoreCase = true) ||
                            it.streamUrl.contains(searchQuery, ignoreCase = true) ||
                            (if (it.type == CctvType.MONITOR) "erMonitor" else "Interkom").contains(searchQuery, ignoreCase = true)
                }

                LoadState(
                    state = cctvs,
                    loading = {
                        SmallCircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    },
                    error = {
                        Text(
                            text = "Gagal memuat cctv, coba lagi",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    success = {
                        val bottomContentPadding = PaddingValues(
                            bottom = contentPadding.calculateBottomPadding()
                        )

                        if (filteredCameras != null) {
                            if (selectedTab == CctvTab.MANAGE) CctvTable(
                                cctvs = filteredCameras,
                                contentPadding = bottomContentPadding,
                                onEditClick = openEditDialog,
                                onDeleteClick = openDeleteDialog
                            ) else CctvGridView(
                                cameras = filteredCameras,
                                gridColumns = gridColumns,
                                contentPadding = bottomContentPadding,
                                isWindowOpen = viewModel::isCctvWindowOpen,
                                shouldAutoMicOn = viewModel::shouldAutoMicOn,
                                onOpenWindow = viewModel::openCctvWindow,
                                onCloseWindow = viewModel::closeCctvWindow,
                                onEditClick = openEditDialog,
                                onDeleteClick = openDeleteDialog,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddCctvDialog(
            camera = selectedCameraForEdit,
            onDismiss = dismissDialogs,
            onConfirm = { name, path, url, type ->
                if (selectedCameraForEdit == null) {
                    viewModel.addCctv(
                        CctvCreate(
                            cameraName = name,
                            path = path,
                            streamUrl = url,
                            type = type
                        )
                    )
                } else {
                    viewModel.updateCctv(
                        CctvUpdate(
                            id = selectedCameraForEdit!!.id,
                            cameraName = name,
                            path = path,
                            streamUrl = url,
                            type = type
                        )
                    )
                }
                dismissDialogs()
            }
        )
    }

    showDeleteDialog?.let { cctv ->
        DeleteCctvDialog(
            camera = cctv,
            onDismiss = dismissDialogs,
            onConfirm = {
                viewModel.deleteCctv(cctv.id)
                dismissDialogs()
            }
        )
    }
}

@Composable
private fun CctvHeader(
    cameraCount: Int?,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${cameraCount ?: "~"} kamera terdaftar",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall
        ) {
            GradientTextButton(
                text = "Tambah Kamera",
                onClick = onAddClick,
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
                shape = RoundedCornerShape(12.dp),
                leading = {
                    Icon(
                        painter = painterResource(GateTikIcons.plus),
                        contentDescription = "tambah kamera",
                        modifier = Modifier.size((LocalTextStyle.current.fontSize.value * 1.5f).dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun CctvStats(
    cameraCount: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CctvStatsCard(
            label = "Online",
            value = cameraCount?.let { "$it Kamera" } ?: "~",
            icon = painterResource(GateTikIcons.wifi),
            iconContainerColor = Emerald500.copy(alpha = 0.1f),
            iconTint = Emerald500,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CctvGridView(
    cameras: List<Cctv>,
    gridColumns: Int,
    contentPadding: PaddingValues,
    isWindowOpen: (Cctv) -> Boolean,
    shouldAutoMicOn: (Cctv) -> Boolean,
    onOpenWindow: (Cctv, autoMicOn: Boolean) -> Unit,
    onCloseWindow: (Cctv) -> Unit,
    onEditClick: (Cctv) -> Unit,
    onDeleteClick: (Cctv) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(cameras) { cctv ->
            CctvGridItem(
                cctv = cctv,
                onFullscreenClick = { onOpenWindow(cctv, false) },
                onMicClick = { onOpenWindow(cctv, true) },
                onSettingsClick = { onEditClick(cctv) },
                onDeleteClick = { onDeleteClick(cctv) }
            )
            if (isWindowOpen(cctv)) CctvWindow(
                cctv = cctv,
                onClose = { onCloseWindow(cctv) },
                autoMicOn = shouldAutoMicOn(cctv)
            )
        }
    }
}

@Composable
private fun CctvTable(
    cctvs: List<Cctv>,
    contentPadding: PaddingValues,
    onEditClick: (Cctv) -> Unit,
    onDeleteClick: (Cctv) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall
    ) {
        val columns = listOf<TableColumn<Cctv>>(
            TableColumn(title = "Kamera", weight = 2f) { camera ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.camera),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Blue500
                    )
                    Text(
                        text = camera.cameraName,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            TableColumn(title = "Path", weight = 1.5f) { camera ->
                Text(
                    text = "/${camera.path}",
                    color = Blue700,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Blue700.copy(alpha = 0.2f))
                        .padding(
                            horizontal = 8.dp,
                            vertical = 2.dp
                        )
                )
            },
            TableColumn(title = "Stream URL", weight = 3f) { camera ->
                Text(
                    text = camera.streamUrl,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            TableColumn(title = "Tipe", weight = 1f) { camera ->
                Text(
                    text = when (camera.type) {
                        CctvType.MONITOR -> "Monitor"
                        CctvType.INTERCOM -> "Interkom"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            TableColumn(title = "Dibuat", weight = 1f) { camera ->
                Text(
                    text = camera.createdAt.toLocalDateString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            TableColumn(title = "Aksi", weight = 1f) { camera ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.squarePen),
                        contentDescription = "Edit",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onEditClick(camera) },
                        tint = Blue500
                    )
                    Icon(
                        painter = painterResource(GateTikIcons.trash),
                        contentDescription = "Hapus",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onDeleteClick(camera) },
                        tint = Red500
                    )
                }
            }
        )

        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding
        ) {
            item {
                Table(
                    columns = columns,
                    items = cctvs
                )
            }
        }
    }
}

@Composable
fun CctvTabSelector(
    selectedTab: CctvTab,
    onTabSelected: (CctvTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDarkMode) White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.05f))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CctvTabItem(
            text = "Monitor",
            icon = GateTikIcons.grid2x2,
            isSelected = selectedTab == CctvTab.MONITOR,
            onClick = { onTabSelected(CctvTab.MONITOR) }
        )
        CctvTabItem(
            text = "Kelola",
            icon = GateTikIcons.layoutPanelLeft,
            isSelected = selectedTab == CctvTab.MANAGE,
            onClick = { onTabSelected(CctvTab.MANAGE) }
        )
    }
}

@Composable
private fun CctvTabItem(
    text: String,
    icon: DrawableResource,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) Blue500 else Color.Transparent
    )
    val contentColor by animateColorAsState(
        if (isSelected) White else MaterialTheme.colorScheme.onSurfaceVariant
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val labelSmall = MaterialTheme.typography.labelSmall

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size((labelSmall.fontSize.value * 1.5f).dp),
            tint = contentColor
        )
        Text(
            text = text,
            style = labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

@DevicePreviews
@Composable
private fun DesktopCctvScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
            DesktopCctvScreen(
                contentPadding = p
            )
        }
    }
}
