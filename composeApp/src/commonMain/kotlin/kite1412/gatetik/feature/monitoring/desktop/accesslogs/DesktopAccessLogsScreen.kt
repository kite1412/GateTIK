package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.LocalCsvExporter
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.EnterIconTrailing
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.Pagination
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.designsystem.component.Select
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.util.Sort
import kite1412.gatetik.feature.monitoring.desktop.ui.component.AccessLogTrend
import kite1412.gatetik.feature.monitoring.desktop.ui.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.ui.util.SideNotificationManager
import kite1412.gatetik.feature.monitoring.desktop.ui.util.desktopBaseModifier
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.network.mock.mockAccessLogs
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.util.timestampString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopAccessLogsScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopAccessLogsViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val pagination by viewModel.pagination.collectAsStateWithLifecycle()
    val accessLogs = viewModel.accessLogs
    val csvExporter = LocalCsvExporter.current
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopAccessLogsScreen(
            userRole = user.role,
            tableAccessLogs = accessLogs,
            trendAccessLogs = viewModel.trendAccessLogs,
            statusFilter = viewModel.selectedStatusFilter,
            trendStatusFilter = viewModel.selectedTrendStatusFilter,
            methodFilter = viewModel.selectedMethodFilter,
            actionFilter = viewModel.selectedActionFilter,
            sort = viewModel.selectedSort,
            currentPage = pagination?.currentPage ?: viewModel.currentPage,
            totalPages = pagination?.lastPage ?: 1,
            itemsPerPage = pagination?.perPage ?: viewModel.perPage,
            sideNotificationManager = viewModel.sideNotificationManager,
            contentPadding = contentPadding,
            onPageChange = viewModel::updateCurrentPage,
            onSearchTextChange = viewModel::updateSearchText,
            onStatusFilterChange = viewModel::updateStatusFilter,
            onTrendStatusFilterChange = viewModel::updateTrendStatusFilter,
            onMethodFilterChange = viewModel::updateMethodFilter,
            onActionFilterChange = viewModel::updateActionFilter,
            onSortChange = viewModel::updateSort,
            onItemsPerPageChange = viewModel::updatePerPage,
            onExportCsv = {
                viewModel.exportCsv(csvExporter)
            },
            onThemeToggle = viewModel::updateDarkMode,
            onRefreshClick = viewModel::refreshAccessLogs,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopAccessLogsScreen(
    userRole: UserRole,
    tableAccessLogs: LoadState<List<AccessLog>>,
    trendAccessLogs: LoadState<List<AccessLog>>,
    statusFilter: AccessStatus?,
    trendStatusFilter: AccessStatus?,
    methodFilter: AccessMethod?,
    actionFilter: AccessAction?,
    sort: Sort,
    currentPage: Int,
    totalPages: Int,
    itemsPerPage: Int,
    sideNotificationManager: SideNotificationManager,
    contentPadding: PaddingValues,
    onStatusFilterChange: (AccessStatus?) -> Unit,
    onTrendStatusFilterChange: (AccessStatus?) -> Unit,
    onMethodFilterChange: (AccessMethod?) -> Unit,
    onActionFilterChange: (AccessAction?) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSortChange: (Sort) -> Unit,
    onPageChange: (Int) -> Unit,
    onItemsPerPageChange: (Int) -> Unit,
    onExportCsv: () -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DesktopLayout(
        title = "Log Akses",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier(),
        onRefreshClick = onRefreshClick,
        sideNotificationManager = sideNotificationManager
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    GlassBox(
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(onClick = onExportCsv)
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(GateTikIcons.download),
                                contentDescription = "ekspor csv",
                                modifier = Modifier.size(18.dp),
                                tint = LocalContentColor.current
                            )
                            Text(
                                text = "Ekspor CSV"
                            )
                        }
                    }
                }
            }

            item {
                AccessLogTrend(
                    accessLogs = trendAccessLogs,
                    headerTrailing = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Status:",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Select(
                                selectedOption = trendStatusFilter?.capitalizedName ?: "Semua",
                                options = listOf("Semua") + AccessStatus.entries.map(AccessStatus::capitalizedName),
                                onOptionSelected = {
                                    onTrendStatusFilterChange(
                                        if (it == "Semua") null
                                        else AccessStatus.valueOf(it.uppercase())
                                    )
                                }
                            )
                        }
                    }
                )
            }

            item {
                FilterSection(
                    onSearchChange = onSearchTextChange,
                    statusFilter = statusFilter,
                    onStatusChange = onStatusFilterChange,
                    methodFilter = methodFilter,
                    onMethodChange = onMethodFilterChange,
                    actionFilter = actionFilter,
                    onActionChange = onActionFilterChange,
                    sort = sort,
                    onSortChange = onSortChange
                )
            }

            item {
                val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
                var itemsPerPage by retain(itemsPerPage) {
                    mutableStateOf("$itemsPerPage")
                }

                AccessLogsTableSection(
                    accessLogs = tableAccessLogs,
                    sort = sort,
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = onPageChange,
                    itemsPerPage = itemsPerPage,
                    onItemsPerPageChange = { itemsPerPage = it },
                    modifier = Modifier.onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyUp && it.key == Key.Enter) {
                            (runCatching {
                                itemsPerPage.toInt()
                            }
                                .getOrNull()
                                ?.let(onItemsPerPageChange) != null)
                                .also { success ->
                                    if (!success) snackbarHostStateWrapper
                                        .showSnackbar("Masukkan angka untuk item per halaman")
                                }
                        } else false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    onSearchChange: (String) -> Unit,
    statusFilter: AccessStatus?,
    onStatusChange: (AccessStatus?) -> Unit,
    methodFilter: AccessMethod?,
    onMethodChange: (AccessMethod?) -> Unit,
    actionFilter: AccessAction?,
    onActionChange: (AccessAction?) -> Unit,
    sort: Sort,
    onSortChange: (Sort) -> Unit
) {
    var searchText by retain {
        mutableStateOf("")
    }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        SearchField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = "Cari log...",
            modifier = Modifier
                .widthIn(min = 400.dp, max = 450.dp)
                .fillMaxWidth()
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                        onSearchChange(searchText)
                        true
                    } else false
                },
            trailing = { EnterIconTrailing() }
        )

        Select(
            label = "Status",
            selectedOption = statusFilter?.capitalizedName ?: "Semua",
            options = listOf("Semua") + AccessStatus.entries.map(AccessStatus::capitalizedName),
            onOptionSelected = {
                onStatusChange(if (it == "Semua") null else AccessStatus.valueOf(it.uppercase()))
            }
        )

        Select(
            label = "Metode",
            selectedOption = methodFilter ?: "Semua",
            options = listOf("Semua") + AccessMethod.entries,
            onOptionSelected = {
                onMethodChange(if (it == "Semua") null else it as AccessMethod)
            },
            optionToString = {
                when (it) {
                    AccessMethod.MOBILE -> "Mobile"
                    AccessMethod.WEB -> "Web"
                    AccessMethod.DESKTOP -> "Desktop"
                    else -> it.toString()
                }
            }
        )

        Select(
            label = "Aksi",
            selectedOption = actionFilter ?: "Semua",
            options = listOf("Semua") + AccessAction.entries,
            onOptionSelected = {
                onActionChange(if (it == "Semua") null else it as AccessAction)
            },
            optionToString = {
                when (it) {
                    AccessAction.OPEN -> "Buka"
                    AccessAction.CLOSE -> "Tutup"
                    AccessAction.ENTRY -> "Masuk"
                    AccessAction.EXIT -> "Keluar"
                    else -> it.toString()
                }
            }
        )

        Select(
            label = "Urutkan",
            selectedOption = sort,
            options = Sort.entries,
            onOptionSelected = onSortChange,
            optionToString = { it.string }
        )
    }
}

@Composable
private fun AccessLogsTableSection(
    accessLogs: LoadState<List<AccessLog>>,
    sort: Sort,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    itemsPerPage: String,
    onItemsPerPageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadState(
            state = accessLogs,
            loading = { Text(it) },
            error = { Text(it) },
            success = { accessLogs ->
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodySmall
                ) {
                    if (accessLogs.isNotEmpty()) Table(
                        columns = listOf(
                            TableColumn(title = "Pengguna", weight = 1.2f) {
                                Text(
                                    text = it.userFullName,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            TableColumn(title = "Role", weight = 1f) {
                                Text(
                                    text = it.userRole.toIdString(),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Aksi", weight = 0.8f) {
                                Text(
                                    text = it.action.capitalizedName,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            TableColumn(title = "Metode", weight = 0.8f) {
                                Text(
                                    text = it.accessMethod.capitalizedName,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Status", weight = 1f) {
                                val (containerColor, contentColor) = when (it.status) {
                                    AccessStatus.SUCCESS -> Emerald500.copy(alpha = 0.1f) to Emerald500
                                    AccessStatus.FAILED -> Red500.copy(alpha = 0.1f) to Red500
                                    AccessStatus.PENDING -> Yellow500.copy(alpha = 0.1f) to Yellow500
                                }
                                Badge(
                                    text = it.status.capitalizedName,
                                    containerColor = containerColor,
                                    contentColor = contentColor
                                )
                            },
                            TableColumn(title = "Catatan", weight = 2.5f) {
                                Text(
                                    text = it.notes ?: "-",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Waktu", weight = 1.5f) {
                                Text(
                                    text = it.createdAt.timestampString,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        ),
                        items = accessLogs
                            .run {
                                when (sort) {
                                    Sort.ASC -> sortedBy { it.createdAt }
                                    Sort.DESC -> sortedByDescending { it.createdAt }
                                }
                            }
                    ) else Text("Log akses tidak ditemukan")
                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Halaman $currentPage dari $totalPages • ${accessLogs.data?.size ?: 0} log",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Pagination(
                currentPage = currentPage,
                lastPage = totalPages,
                onPageChange = onPageChange,
                itemsPerPage = itemsPerPage,
                onItemsPerPageChange = onItemsPerPageChange
            )
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopAccessLogsScreenPreview() {
    GateTikTheme(darkMode = isSystemInDarkTheme()) {
        CompositionLocalProvider(
            LocalScaffoldComponentsController provides MockScaffoldComponentController
        ) {
            Scaffold { p ->
                DesktopAccessLogsScreen(
                    userRole = UserRole.ADMIN,
                    onSearchTextChange = {},
                    statusFilter = null,
                    trendStatusFilter = null,
                    sideNotificationManager = SideNotificationManager(rememberCoroutineScope()),
                    onStatusFilterChange = {},
                    onTrendStatusFilterChange = {},
                    methodFilter = null,
                    onMethodFilterChange = {},
                    actionFilter = null,
                    onActionFilterChange = {},
                    sort = Sort.ASC,
                    onSortChange = {},
                    tableAccessLogs = LoadState.Success(mockAccessLogs),
                    trendAccessLogs = LoadState.Success(mockAccessLogs),
                    currentPage = 1,
                    totalPages = 1,
                    onPageChange = {},
                    itemsPerPage = 15,
                    onItemsPerPageChange = {},
                    onExportCsv = {},
                    contentPadding = PaddingValues(24.dp),
                    onThemeToggle = {},
                    onRefreshClick = {},
                    modifier = Modifier
                        .fillMaxSize()
                        .radialBackground()
                        .padding(p)
                )
            }
        }
    }
}
