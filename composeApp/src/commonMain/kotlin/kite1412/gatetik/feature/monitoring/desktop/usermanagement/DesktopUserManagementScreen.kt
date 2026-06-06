package kite1412.gatetik.feature.monitoring.desktop.usermanagement

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.FilterChip
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.Pagination
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.Emerald700
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Red600_90
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.LocalRemoteImageResolver
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.rememberRemoteImageLoader
import kite1412.gatetik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.network.mock.mockUser
import kite1412.gatetik.ui.component.ActionIconButton
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.compositionlocal.LocalWindowBlurRequester
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.data
import kite1412.gatetik.util.timestampString
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopUserManagementScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopUserManagementViewModel = koinViewModel()
) {
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val pagination by viewModel.pagination.collectAsStateWithLifecycle()

    user?.let { user ->
        CompositionLocalProvider(
            LocalRemoteImageResolver provides viewModel.kmpResolver
        ) {
            DesktopUserManagementContent(
                userRole = user.role,
                users = viewModel.users,
                currentPage = pagination?.currentPage ?: 1,
                totalPages = pagination?.lastPage ?: 1,
                itemsPerPage = pagination?.perPage ?: 15,
                contentPadding = contentPadding,
                onSearchTextChange = viewModel::onSearchTextChange,
                selectedRole = viewModel.selectedRole,
                onRoleFilterChange = viewModel::onRoleFilterChange,
                selectedStatus = viewModel.selectedStatus,
                onStatusFilterChange = viewModel::onStatusFilterChange,
                onThemeToggle = viewModel::updateDarkMode,
                onPageChange = {},
                onItemsPerPageChange = viewModel::updatePerPage,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DesktopUserManagementContent(
    userRole: UserRole,
    users: LoadState<List<User>>,
    currentPage: Int,
    totalPages: Int,
    itemsPerPage: Int,
    contentPadding: PaddingValues,
    onSearchTextChange: (String) -> Unit,
    selectedRole: UserRole?,
    onRoleFilterChange: (UserRole?) -> Unit,
    selectedStatus: UserStatus?,
    onStatusFilterChange: (UserStatus?) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onPageChange: (Int) -> Unit,
    onItemsPerPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowBlurRequester = LocalWindowBlurRequester.current
    var searchText by retain {
        mutableStateOf("")
    }
    var popup by retain {
        mutableStateOf("")
    }
    var selectedUser by retain { mutableStateOf<User?>(null) }
    val requestPopup = { name: String, user: User ->
        popup = name
        selectedUser = user
        windowBlurRequester.applyWindowBlur()
    }
    val dismissPopup = {
        popup = ""
        windowBlurRequester.removeWindowBlue()
    }

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
                    onValueChange = { searchText = it },
                    placeholder = "Cari nama, email, NPM/NIP, atau telepon...",
                    modifier = Modifier
                        .widthIn(min = 400.dp, max = 500.dp)
                        .fillMaxWidth()
                        .onPreviewKeyEvent {
                            if (it.type == KeyEventType.KeyUp && it.key == Key.Enter) {
                                onSearchTextChange(searchText)
                                true
                            } else false
                        }
                )

                Icon(
                    painter = painterResource(GateTikIcons.funnel),
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
                    UserTable(
                        users = users,
                        onDetailClick = { requestPopup("detail", it) },
                        onActivateUserClick = { requestPopup("activate-user", it) },
                        onEditUserClick = { requestPopup("edit-user", it) },
                        onDeleteUserClick = { requestPopup("delete-user", it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
                        var itemsPerPage by retain(itemsPerPage) {
                            mutableStateOf("$itemsPerPage")
                        }

                        Text(
                            text = "Halaman $currentPage dari $totalPages • ${users.data?.size ?: "~"} pengguna",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Pagination(
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
    }
    selectedUser?.let {
        when (popup) {
            "detail" -> UserDetailDialog(
                user = it,
                onDismissRequest = dismissPopup
            )
            "activate-user" -> {}
            "edit-user" -> {}
            "delete-user" -> {}
            else -> {}
        }
    } 
}

@Composable
private fun UserTable(
    users: LoadState<List<User>>,
    onDetailClick: (User) -> Unit,
    onActivateUserClick: (User) -> Unit,
    onEditUserClick: (User) -> Unit,
    onDeleteUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodySmall
    ) {
        LoadState(
            state = users,
            loading = { Text(it) },
            error = { Text(it) },
            success = {
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
                            Text(user.institutionNumber)
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
                        TableColumn("TERDAFTAR", 2f) { user ->
                            Text(
                                text = user.createdAt.timestampString,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        TableColumn("AKSI", 2f) { user -> 
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                itemVerticalAlignment = Alignment.CenterVertically
                            ) {
                                ActionIconButton(
                                    icon = GateTikIcons.eyeOpen,
                                    onClick = { onDetailClick(user) },
                                    tint = Blue500
                                )
                                ActionIconButton(
                                    icon = GateTikIcons.userCheck,
                                    onClick = { onActivateUserClick(user) },
                                    tint = Emerald500
                                )
                                ActionIconButton(
                                    icon = GateTikIcons.userPen,
                                    onClick = { onEditUserClick(user) },
                                    tint = Yellow500
                                )
                                ActionIconButton(
                                    icon = GateTikIcons.trash,
                                    onClick = { onDeleteUserClick(user) },
                                    tint = Red500
                                )
                            }
                        }
                    ),
                    items = it,
                    modifier = modifier
                )
            }
        )
    }
}

@Composable
private fun UserDetailDialog(
    user: User,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBoxDialog(
        title = "Detail Pengguna",
        desc = "Detail informasi pengguna",
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        with(user) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    UserDetailField(
                        key = "nama lengkap",
                        value = fullName
                    )
                }
                item {
                    UserDetailField(
                        key = "email",
                        value = email
                    )
                }
                item {
                    UserDetailField(
                        key = "npm/nip",
                        value = institutionNumber
                    )
                }
                item {
                    UserDetailField(
                        key = "no. telepon",
                        value = phoneNumber ?: "-"
                    )
                }
                item {
                    UserDetailField(
                        key = "role",
                        value = role.toIdString()
                    )
                }
                item {
                    UserDetailField(
                        key = "status",
                        value = status.capitalizedName
                    )
                }
                item {
                    UserDetailField(
                        key = "terdaftar",
                        value = createdAt.timestampString
                    )
                }
                item {
                    UserDetailField(
                        key = "login terakhir",
                        value = lastLoginAt?.timestampString ?: "-"
                    )
                }
                if (user.role == UserRole.STUDENT) item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "KTM",
                            style = MaterialTheme.typography.bodySmall,
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                        GlassBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f/10f),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            if (ktmPath == null) Text(
                                text = "File KTM tidak ditemukan",
                                modifier = Modifier.align(Alignment.Center),
                                color = Red600_90,
                                style = MaterialTheme.typography.bodySmall
                            ) else {
                                val remoteImageResolver = LocalRemoteImageResolver.current
                                val remoteImageLoader = rememberRemoteImageLoader(user.id)
                                var imageBitmap by retain {
                                    mutableStateOf<ImageBitmap?>(null)
                                }
                                var showFullScreen by retain {
                                    mutableStateOf(false)
                                }

                                LaunchedEffect(Unit) {
                                    if (imageBitmap == null)
                                        imageBitmap = remoteImageLoader.resolveWith(remoteImageResolver)
                                }
                                imageBitmap?.let { imageBitmap ->
                                    Box {
                                        Image(
                                            bitmap = imageBitmap,
                                            contentDescription = "KTM",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.FillBounds
                                        )
                                        IconButton(
                                            onClick = { showFullScreen = true },
                                            modifier = Modifier.align(Alignment.TopEnd)
                                        ) {
                                            Icon(
                                                painter = painterResource(GateTikIcons.zoomIn),
                                                contentDescription = "full screen"
                                            )
                                        }
                                    }

                                    if (showFullScreen) Dialog(
                                        onDismissRequest = { showFullScreen = false },
                                        properties = DialogProperties(
                                            dismissOnBackPress = true,
                                            dismissOnClickOutside = true,
                                            usePlatformDefaultWidth = false
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = null
                                                ) { showFullScreen = false }
                                                .padding(32.dp)
                                        ) {
                                            Image(
                                                bitmap = imageBitmap,
                                                contentDescription = "KTM",
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            GlassBox(
                                                modifier = Modifier.align(Alignment.TopEnd),
                                                contentPadding = PaddingValues(0.dp),
                                                shape = CircleShape
                                            ) {
                                                Icon(
                                                    painter = painterResource(GateTikIcons.x),
                                                    contentDescription = "batal",
                                                    modifier = Modifier
                                                        .clickable { showFullScreen = false }
                                                        .padding(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserDetailField(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val keyColor = LocalContentColor.current.copy(alpha = 0.6f)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall
            ) {
                Text(
                    text = key.uppercase(),
                    color = keyColor
                )
                Text(value)
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = keyColor,
            thickness = 1.dp
        )
    }
}

@Composable
private fun GlassBoxDialog(
    title: String,
    desc: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    ) {
        GlassBox(
            modifier = modifier
                .widthIn(min = 500.dp, max = 700.dp)
                .fillMaxWidth(),
            contentPadding = contentPadding
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(contentPadding.calculateBottomPadding())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(title)
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                    }
                    GlassBox(
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.x),
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(onClick = onDismissRequest)
                                .padding(4.dp)
                        )
                    }
                }
                content()
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
        GateTikTheme(darkMode = isSystemInDarkTheme()) {
            Scaffold { p ->
                DesktopUserManagementContent(
                    userRole = UserRole.ADMIN,
                    users = LoadState.Success(listOf(mockUser, mockUser)),
                    contentPadding = p,
                    onSearchTextChange = {},
                    selectedRole = null,
                    onRoleFilterChange = {},
                    selectedStatus = null,
                    onStatusFilterChange = {},
                    onThemeToggle = {},
                    currentPage = 1,
                    totalPages = 1,
                    itemsPerPage = 15,
                    onPageChange = {},
                    onItemsPerPageChange = {}
                )
            }
        }
    }
}
