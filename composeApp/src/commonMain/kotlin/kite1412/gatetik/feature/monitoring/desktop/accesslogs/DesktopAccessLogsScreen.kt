package kite1412.gatetik.feature.monitoring.desktop.accesslogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.Pagination
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.designsystem.component.Select
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.util.Sort
import kite1412.gatetik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.gatetik.model.AccessAction
import kite1412.gatetik.model.AccessLog
import kite1412.gatetik.model.AccessMethod
import kite1412.gatetik.model.AccessStatus
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.network.mock.mockAccessLogs
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
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

    user?.let { user ->
        DesktopAccessLogsScreen(
            userRole = user.role,
            accessLogs = accessLogs,
            searchText = viewModel.searchText,
            onSearchTextChange = viewModel::updateSearchText,
            statusFilter = viewModel.selectedStatusFilter,
            onStatusFilterChange = viewModel::updateStatusFilter,
            methodFilter = viewModel.selectedMethodFilter,
            onMethodFilterChange = viewModel::updateMethodFilter,
            actionFilter = viewModel.selectedActionFilter,
            onActionFilterChange = viewModel::updateActionFilter,
            sort = viewModel.selectedSort,
            onSortChange = viewModel::updateSort,
            currentPage = pagination?.currentPage ?: 1,
            totalPages = pagination?.totalPages ?: 1,
            onPageChange = {},
            itemsPerPage = (pagination?.perPage ?: 15).toString(),
            onItemsPerPageChange = {},
            onExportCsv = viewModel::exportCsv,
            contentPadding = contentPadding,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopAccessLogsScreen(
    userRole: UserRole,
    accessLogs: LoadState<List<AccessLog>>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    statusFilter: AccessStatus?,
    onStatusFilterChange: (AccessStatus?) -> Unit,
    methodFilter: AccessMethod?,
    onMethodFilterChange: (AccessMethod?) -> Unit,
    actionFilter: AccessAction?,
    onActionFilterChange: (AccessAction?) -> Unit,
    sort: Sort,
    onSortChange: (Sort) -> Unit,
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    itemsPerPage: String,
    onItemsPerPageChange: (String) -> Unit,
    onExportCsv: () -> Unit,
    contentPadding: PaddingValues,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    DesktopLayout(
        title = "Log Akses",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier()
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
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Ekspor CSV"
                            )
                        }
                    }
                }
            }

            item {
                AccessTrendSection(
                    statusFilter = statusFilter,
                    onStatusFilterChange = onStatusFilterChange
                )
            }

            item {
                FilterSection(
                    searchText = searchText,
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
                AccessLogsTableSection(
                    accessLogs = accessLogs,
                    sort = sort,
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = onPageChange,
                    itemsPerPage = itemsPerPage,
                    onItemsPerPageChange = onItemsPerPageChange
                )
            }
        }
    }
}

@Composable
private fun AccessTrendSection(
    statusFilter: AccessStatus?,
    onStatusFilterChange: (AccessStatus?) -> Unit
) {
    GlassBox {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TREN AKSES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "24 jam terakhir",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Select(
                    label = "Status",
                    selectedOption = statusFilter ?: "Semua",
                    options = listOf("Semua") + AccessStatus.entries,
                    onOptionSelected = {
                        onStatusFilterChange(if (it == "Semua") null else it as AccessStatus)
                    },
                    optionToString = {
                        when (it) {
                            AccessStatus.SUCCESS -> "Sukses"
                            AccessStatus.FAILED -> "Gagal"
                            AccessStatus.PENDING -> "Tertunda"
                            else -> it.toString()
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Transparent)
            ) {
                // Placeholder for chart (consistent with Dashboard)
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    repeat(5) { i ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${4 - i}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(24.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("00:00", "04:00", "08:00", "12:00", "16:00", "20:00").forEach { time ->
                        Text(
                            text = time,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Blue500)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    searchText: String,
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
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        SearchField(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = "Cari log...",
            modifier = Modifier
                .widthIn(min = 400.dp, max = 450.dp)
                .fillMaxWidth()
        )

        Select(
            label = "Status",
            selectedOption = statusFilter ?: "Semua",
            options = listOf("Semua") + AccessStatus.entries,
            onOptionSelected = {
                onStatusChange(if (it == "Semua") null else it as AccessStatus)
            },
            optionToString = {
                when (it) {
                    AccessStatus.SUCCESS -> "Sukses"
                    AccessStatus.FAILED -> "Gagal"
                    AccessStatus.PENDING -> "Tertunda"
                    else -> it.toString()
                }
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
                            TableColumn(title = "Pengguna", weight = 1.2f) { _ ->
                                Text(
                                    text = "John Doe",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            TableColumn(title = "Role", weight = 1f) { _ ->
                                Text(
                                    text = "Mahasiswa",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Aksi", weight = 0.8f) { log ->
                                Text(
                                    text = when (log.action) {
                                        AccessAction.OPEN -> "Buka"
                                        AccessAction.CLOSE -> "Tutup"
                                        AccessAction.ENTRY -> "Masuk"
                                        AccessAction.EXIT -> "Keluar"
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            TableColumn(title = "Metode", weight = 0.8f) { log ->
                                Text(
                                    text = when (log.accessMethod) {
                                        AccessMethod.MOBILE -> "Mobile"
                                        AccessMethod.WEB -> "Web"
                                        AccessMethod.DESKTOP -> "Desktop"
                                    },
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Status", weight = 1f) { log ->
                                val (containerColor, contentColor, text) = when (log.status) {
                                    AccessStatus.SUCCESS -> Triple(Emerald500.copy(alpha = 0.1f), Emerald500, "Sukses")
                                    AccessStatus.FAILED -> Triple(Red500.copy(alpha = 0.1f), Red500, "Gagal")
                                    AccessStatus.PENDING -> Triple(Yellow500.copy(alpha = 0.1f), Yellow500, "Tertunda")
                                }
                                Badge(
                                    text = text,
                                    containerColor = containerColor,
                                    contentColor = contentColor
                                )
                            },
                            TableColumn(title = "Catatan", weight = 2.5f) { log ->
                                Text(
                                    text = log.notes ?: "-",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            TableColumn(title = "Waktu", weight = 1.5f) { log ->
                                Text(
                                    text = log.createdAt.timestampString,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        ),
                        items = accessLogs
                            .run {
                                when (sort) {
                                    Sort.ASC -> sortedByDescending { it.createdAt }
                                    Sort.DESC -> sortedBy { it.createdAt }
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
                totalPages = 1,
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
                    searchText = "",
                    onSearchTextChange = {},
                    statusFilter = null,
                    onStatusFilterChange = {},
                    methodFilter = null,
                    onMethodFilterChange = {},
                    actionFilter = null,
                    onActionFilterChange = {},
                    sort = Sort.ASC,
                    onSortChange = {},
                    accessLogs = LoadState.Success(mockAccessLogs),
                    currentPage = 1,
                    totalPages = 1,
                    onPageChange = {},
                    itemsPerPage = "15",
                    onItemsPerPageChange = {},
                    onExportCsv = {},
                    contentPadding = PaddingValues(24.dp),
                    onThemeToggle = {},
                    modifier = Modifier
                        .fillMaxSize()
                        .radialBackground()
                        .padding(p)
                )
            }
        }
    }
}
