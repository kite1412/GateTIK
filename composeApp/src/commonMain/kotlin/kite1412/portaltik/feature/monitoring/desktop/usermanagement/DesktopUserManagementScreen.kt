package kite1412.portaltik.feature.monitoring.desktop.usermanagement

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.FilterChip
import kite1412.portaltik.designsystem.component.Pagination
import kite1412.portaltik.designsystem.component.SearchField
import kite1412.portaltik.designsystem.component.Table
import kite1412.portaltik.designsystem.component.TableColumn
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.Emerald500
import kite1412.portaltik.designsystem.theme.Emerald700
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red500
import kite1412.portaltik.designsystem.theme.Yellow500
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.portaltik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.model.UserStatus
import kite1412.portaltik.ui.component.ActionIconButton
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.MockScaffoldComponentController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopUserManagementScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopUserManagementViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsState()
    
    user?.let { user ->
        DesktopUserManagementContent(
            userRole = user.role,
            users = users,
            searchText = viewModel.searchText,
            contentPadding = contentPadding,
            onSearchTextChange = viewModel::onSearchTextChange,
            selectedRole = viewModel.selectedRole,
            onRoleFilterChange = viewModel::onRoleFilterChange,
            selectedStatus = viewModel.selectedStatus,
            onStatusFilterChange = viewModel::onStatusFilterChange,
            onThemeToggle = viewModel::updateDarkMode,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DesktopUserManagementContent(
    userRole: UserRole,
    users: List<User>,
    searchText: String,
    contentPadding: PaddingValues,
    onSearchTextChange: (String) -> Unit,
    selectedRole: UserRole?,
    onRoleFilterChange: (UserRole?) -> Unit,
    selectedStatus: UserStatus?,
    onStatusFilterChange: (UserStatus?) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    DesktopLayout(
        title = "Manajemen Pengguna",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier()
    ) {
        Column(
            modifier = Modifier.padding(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                SearchField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = "Cari nama, email, NPM/NIP, atau telepon...",
                    modifier = Modifier
                        .widthIn(min = 400.dp, max = 500.dp)
                        .fillMaxWidth()
                )

                Icon(
                    painter = painterResource(PortalTikIcons.funnel),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                FilterChip(
                    text = "Semua",
                    isSelected = selectedRole == null,
                    onClick = { onRoleFilterChange(null) },
                    selectedContainerColor = Blue500
                )
                UserRole.entries.forEach { role ->
                    FilterChip(
                        text = role.toIdString(),
                        isSelected = selectedRole == role,
                        onClick = { onRoleFilterChange(role) }
                    )
                }

                FilterChip(
                    text = "Semua",
                    isSelected = selectedStatus == null,
                    onClick = { onStatusFilterChange(null) },
                    selectedContainerColor = Emerald700
                )
                UserStatus.entries.forEach { status ->
                    FilterChip(
                        text = when (status) {
                            UserStatus.PENDING -> "Tertunda"
                            UserStatus.ACTIVE -> "Aktif"
                            UserStatus.SUSPENDED -> "Ditangguhkan"
                        },
                        isSelected = selectedStatus == status,
                        onClick = { onStatusFilterChange(status) },
                        selectedContainerColor = when (status) {
                            UserStatus.ACTIVE -> Emerald700
                            UserStatus.PENDING -> Yellow500
                            UserStatus.SUSPENDED -> Red500
                        }
                    )
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    bottom = contentPadding.calculateBottomPadding()
                )
            ) {
                item {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodySmall
                    ) {
                        Table(
                            columns = listOf(
                                TableColumn("NAMA", 2f) { user ->
                                    Text(
                                        text = user.fullName,
                                        style = LocalTextStyle.current.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                },
                                TableColumn("ROLE", 1.5f) { user ->
                                    Text(user.role.toIdString())
                                },
                                TableColumn("EMAIL", 2.5f) { user ->
                                    Text(user.email)
                                },
                                TableColumn("NPM/NIP", 1.5f) { user ->
                                    Text(user.instituteNumber)
                                },
                                TableColumn("STATUS", 1.5f) { user ->
                                    val (containerColor, contentColor) = when (user.status) {
                                        UserStatus.ACTIVE -> Emerald500.copy(alpha = 0.1f) to Emerald500
                                        UserStatus.PENDING -> Yellow500.copy(alpha = 0.1f) to Yellow500
                                        UserStatus.SUSPENDED -> Red500.copy(alpha = 0.1f) to Red500
                                    }
                                    Badge(
                                        text = user.status.name,
                                        containerColor = containerColor,
                                        contentColor = contentColor
                                    )
                                },
                                TableColumn("TERDAFTAR", 2f) {
                                    Text(
                                        text = "31 Mei 2026, 12:49:37", // Dummy
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                TableColumn("AKSI", 1.5f) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        ActionIconButton(
                                            icon = PortalTikIcons.eyeOpen,
                                            onClick = { },
                                            tint = Blue500
                                        )
                                        ActionIconButton(
                                            icon = PortalTikIcons.userCheck,
                                            onClick = { },
                                            tint = Emerald500
                                        )
                                        ActionIconButton(
                                            icon = PortalTikIcons.userPen,
                                            onClick = { },
                                            tint = Yellow500
                                        )
                                        ActionIconButton(
                                            icon = PortalTikIcons.trash,
                                            onClick = { },
                                            tint = Red500
                                        )
                                    }
                                }
                            ),
                            items = users,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Halaman 1 dari 1 • ${users.size} pengguna",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Pagination(
                            currentPage = 1,
                            totalPages = 1,
                            onPageChange = { },
                            itemsPerPage = 15,
                            onItemsPerPageChange = { }
                        )
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopUserManagementScreenPreview() {
    CompositionLocalProvider(
        LocalScaffoldComponentsController provides MockScaffoldComponentController
    ) {
        PortalTikTheme(darkMode = isSystemInDarkTheme()) {
            Scaffold { p ->
                DesktopUserManagementContent(
                    userRole = UserRole.ADMIN,
                    users = listOf(
                        User(1, "John Doe", "student@example.com", UserRole.STUDENT, UserStatus.ACTIVE, "STU001"),
                        User(2, "Jane Smith", "student2@example.com", UserRole.STUDENT, UserStatus.PENDING, "STU002")
                    ),
                    searchText = "",
                    contentPadding = p,
                    onSearchTextChange = {},
                    selectedRole = null,
                    onRoleFilterChange = {},
                    selectedStatus = null,
                    onStatusFilterChange = {},
                    onThemeToggle = {}
                )
            }
        }
    }
}
