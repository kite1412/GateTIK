package kite1412.gatetik.feature.monitoring.desktop.cctv

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GradientTextButton
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.StatusIndicator
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.AddCctvDialog
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvActionBar
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvGridItem
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvStatsCard
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.DeleteCctvDialog
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.ui.compositionlocal.LocalWindowBlurRequester
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.util.toLocalDateString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopCctvViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val cameras by viewModel.cameras.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val gridColumns by viewModel.gridColumns.collectAsStateWithLifecycle()

    val windowBlurRequester = LocalWindowBlurRequester.current

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Cctv?>(null) }
    var selectedCameraForEdit by remember { mutableStateOf<Cctv?>(null) }

    val openAddDialog = {
        showAddDialog = true
        windowBlurRequester.applyWindowBlur()
    }

    val openEditDialog = { camera: Cctv ->
        selectedCameraForEdit = camera
        showAddDialog = true
        windowBlurRequester.applyWindowBlur()
    }

    val openDeleteDialog = { camera: Cctv ->
        showDeleteDialog = camera
        windowBlurRequester.applyWindowBlur()
    }

    val dismissDialogs = {
        showAddDialog = false
        selectedCameraForEdit = null
        showDeleteDialog = null
        windowBlurRequester.removeWindowBlue()
    }

    user?.let { user ->
        DesktopLayout(
            title = "Monitoring CCTV",
            userRole = user.role,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier.desktopBaseModifier()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${cameras.size} kamera terdaftar",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodySmall
                    ) {
                        GradientTextButton(
                            text = "Tambah Kamera",
                            onClick = openAddDialog,
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CctvStatsCard(
                        label = "Online",
                        value = "${cameras.count { it.isActive }} Kamera",
                        icon = painterResource(GateTikIcons.wifi),
                        iconContainerColor = Emerald500.copy(alpha = 0.1f),
                        iconTint = Emerald500,
                        modifier = Modifier.weight(1f)
                    )
                    CctvStatsCard(
                        label = "Offline",
                        value = "${cameras.count { !it.isActive }} Kamera",
                        icon = painterResource(GateTikIcons.parkingOff), // Reusing similar icon
                        iconContainerColor = Red500.copy(alpha = 0.1f),
                        iconTint = Red500,
                        modifier = Modifier.weight(1f)
                    )
                }

                CctvActionBar(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::updateSelectedTab,
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    gridColumns = gridColumns,
                    onGridColumnsChange = viewModel::updateGridColumns,
                    onAddClick = openAddDialog
                )

                val filteredCameras = cameras.filter {
                    it.cameraName.contains(searchQuery, ignoreCase = true) ||
                            it.path.contains(searchQuery, ignoreCase = true) ||
                            it.streamUrl.contains(searchQuery, ignoreCase = true)
                }

                if (selectedTab == CctvTab.MONITOR) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridColumns),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredCameras) { camera ->
                            CctvGridItem(
                                camera = camera,
                                onFullscreenClick = { /* TODO */ },
                                onSettingsClick = { openEditDialog(camera) },
                                onDeleteClick = { openDeleteDialog(camera) }
                            )
                        }
                    }
                } else {
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
                            TableColumn(title = "Path", weight = 1f) { camera ->
                                Text(
                                    text = camera.path,
                                    color = Blue500
                                )
                            },
                            TableColumn(title = "Stream URL", weight = 3f) { camera ->
                                Text(
                                    text = camera.streamUrl,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Status", weight = 1f) { camera ->
                                Badge(
                                    text = if (camera.isActive) "online" else "offline",
                                    containerColor = if (camera.isActive) Emerald500.copy(alpha = 0.1f) else Red500.copy(alpha = 0.1f),
                                    contentColor = if (camera.isActive) Emerald500 else Red500,
                                    leadingIcon = {
                                        StatusIndicator(color = if (camera.isActive) Emerald500 else Red500)
                                    }
                                )
                            },
                            TableColumn(title = "Dibuat", weight = 1.5f) { camera ->
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
                                        painter = painterResource(GateTikIcons.userPen),
                                        contentDescription = "Edit",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { openEditDialog(camera) },
                                        tint = Blue500
                                    )
                                    Icon(
                                        painter = painterResource(GateTikIcons.trash),
                                        contentDescription = "Hapus",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { openDeleteDialog(camera) },
                                        tint = Red500
                                    )
                                }
                            }
                        )

                        Table(
                            columns = columns,
                            items = filteredCameras,
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCctvDialog(
            camera = selectedCameraForEdit,
            onDismiss = dismissDialogs,
            onConfirm = { name, path, url ->
                if (selectedCameraForEdit == null) {
                    viewModel.addCamera(name, path, url)
                } else {
                    // TODO: Update camera logic if needed
                }
                dismissDialogs()
            }
        )
    }

    showDeleteDialog?.let { camera ->
        DeleteCctvDialog(
            camera = camera,
            onDismiss = dismissDialogs,
            onConfirm = {
                viewModel.deleteCamera(camera)
                dismissDialogs()
            }
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
