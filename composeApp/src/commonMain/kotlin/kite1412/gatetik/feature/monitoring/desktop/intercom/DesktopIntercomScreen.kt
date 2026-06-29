package kite1412.gatetik.feature.monitoring.desktop.intercom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvActionBar
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvGridItem
import kite1412.gatetik.feature.monitoring.desktop.cctv.component.CctvPaginationHeader
import kite1412.gatetik.feature.monitoring.desktop.ui.component.CctvWindow
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopIntercomScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopIntercomViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val intercomCameras = viewModel.intercomCameras
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()

    var tempSearchQuery by retain(searchQuery) { mutableStateOf(searchQuery) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }

    user?.let { user ->
        DesktopLayout(
            title = "Panggilan Interkom",
            userRole = user.role,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier.desktopBaseModifier(),
            onRefreshClick = viewModel::refreshIntercomCameras
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
                IntercomHeader(
                    cameraCount = intercomCameras.data?.size
                )

                CctvActionBar(
                    searchQuery = tempSearchQuery,
                    onSearchQueryChange = { tempSearchQuery = it },
                    modifier = Modifier.onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                            viewModel.updateSearchQuery(tempSearchQuery)
                            true
                        } else false
                    }
                )

                val filteredCameras = intercomCameras.data?.filter {
                    it.cameraName.contains(searchQuery, ignoreCase = true) ||
                            it.path.contains(searchQuery, ignoreCase = true) ||
                            it.streamUrl.contains(searchQuery, ignoreCase = true)
                }

                LoadState(
                    state = intercomCameras,
                    loading = {
                        SmallCircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    },
                    error = {
                        Text(
                            text = "Gagal memuat interkom, coba lagi",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    success = {
                        val bottomContentPadding = PaddingValues(
                            bottom = contentPadding.calculateBottomPadding()
                        )

                        if (filteredCameras != null) {
                            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                                val availableWidth = maxWidth

                                val columns = when {
                                    availableWidth > 1400.dp -> 4
                                    availableWidth > 800.dp -> 3
                                    else -> 2
                                }
                                val pageSize = columns * 2

                                val totalPages = (filteredCameras.size + pageSize - 1) / pageSize
                                val startIndex = currentPage * pageSize
                                val endIndex = (startIndex + pageSize).coerceAtMost(filteredCameras.size)
                                val pagedCameras = filteredCameras.subList(startIndex, endIndex)

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    CctvPaginationHeader(
                                        currentPage = currentPage,
                                        totalPages = totalPages,
                                        startIndex = startIndex,
                                        endIndex = endIndex,
                                        totalItems = filteredCameras.size,
                                        onPageChange = viewModel::updateCurrentPage
                                    )

                                    IntercomGridView(
                                        cameras = pagedCameras,
                                        gridColumns = columns,
                                        contentPadding = bottomContentPadding,
                                        isWindowOpen = viewModel::isCctvWindowOpen,
                                        shouldAutoMicOn = viewModel::shouldAutoMicOn,
                                        onOpenWindow = viewModel::openCctvWindow,
                                        onCloseWindow = viewModel::closeCctvWindow,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun IntercomHeader(
    cameraCount: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${cameraCount ?: "~"} kamera interkom terdaftar",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IntercomGridView(
    cameras: List<Cctv>,
    gridColumns: Int,
    contentPadding: PaddingValues,
    isWindowOpen: (Cctv) -> Boolean,
    shouldAutoMicOn: (Cctv) -> Boolean,
    onOpenWindow: (Cctv, autoMicOn: Boolean) -> Unit,
    onCloseWindow: (Cctv) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val spacing = 16.dp
        val availableHeight = maxHeight - contentPadding.calculateTopPadding() - contentPadding.calculateBottomPadding()
        val maxItemHeight = (availableHeight - spacing) / 2

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false,
            modifier = Modifier.fillMaxSize()
        ) {
            items(cameras) { cctv ->
                CctvGridItem(
                    cctv = cctv,
                    onFullscreenClick = { onOpenWindow(cctv, false) },
                    onMicClick = { onOpenWindow(cctv, true) },
                    onSettingsClick = { /* No settings in Intercom view */ },
                    onDeleteClick = { /* No delete in Intercom view */ },
                    modifier = Modifier.heightIn(max = maxItemHeight)
                )
                if (isWindowOpen(cctv)) CctvWindow(
                    cctv = cctv,
                    onClose = { onCloseWindow(cctv) },
                    autoMicOn = shouldAutoMicOn(cctv)
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopIntercomScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
            DesktopIntercomScreen(
                contentPadding = p
            )
        }
    }
}
