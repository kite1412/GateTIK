package kite1412.gatetik.feature.monitoring.desktop.usermanagement

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.Badge
import kite1412.gatetik.designsystem.component.FilterChip
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.GlassBoxDialog
import kite1412.gatetik.designsystem.component.GradientTextButton
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.component.Pagination
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.designsystem.component.Select
import kite1412.gatetik.designsystem.component.Table
import kite1412.gatetik.designsystem.component.TableColumn
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald500
import kite1412.gatetik.designsystem.theme.Emerald700
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray200
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Red600_90
import kite1412.gatetik.designsystem.theme.RoyalBlue800_50
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White50
import kite1412.gatetik.designsystem.theme.Yellow500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.domain.model.UserCreate
import kite1412.gatetik.domain.model.UserUpdate
import kite1412.gatetik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.compositionlocal.LocalRemoteImageResolver
import kite1412.gatetik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.network.mock.mockUser
import kite1412.gatetik.rememberRemoteImageLoader
import kite1412.gatetik.ui.component.ActionIconButton
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.compositionlocal.LocalWindowBlurRequester
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.UiEvent
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
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val pagination by viewModel.pagination.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        CompositionLocalProvider(
            LocalRemoteImageResolver provides viewModel.ktmResolver
        ) {
            DesktopUserManagementScreen(
                userRole = user.role,
                users = viewModel.users,
                currentPage = pagination?.currentPage ?: 1,
                totalPages = pagination?.lastPage ?: 1,
                itemsPerPage = pagination?.perPage ?: viewModel.perPage,
                contentPadding = contentPadding,
                onSearchTextChange = viewModel::onSearchTextChange,
                selectedRole = viewModel.selectedRole,
                onRoleFilterChange = viewModel::onRoleFilterChange,
                selectedStatus = viewModel.selectedStatus,
                onStatusFilterChange = viewModel::onStatusFilterChange,
                onThemeToggle = viewModel::updateDarkMode,
                onPageChange = viewModel::updateCurrentPage,
                onItemsPerPageChange = viewModel::updatePerPage,
                onEditUser = viewModel::editUser,
                onAddUser = viewModel::addUser,
                onDeleteUser = viewModel::deleteUser,
                onActivateUser = viewModel::activateUser,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DesktopUserManagementScreen(
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
    onEditUser: (data: UserUpdate) -> Unit,
    onAddUser: (data: UserCreate) -> Unit,
    onDeleteUser: (User) -> Unit,
    onActivateUser: (User) -> Unit,
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
    val requestPopup = { name: String, user: User? ->
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

                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodySmall
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = users.data?.let { users ->
                                "${users.size} pengguna terdaftar"
                            } ?: "",
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                        GradientTextButton(
                            text = "Tambah Pengguna",
                            onClick = { requestPopup("add-user", null) },
                            leading = {
                                Icon(
                                    painter = painterResource(GateTikIcons.userPlus),
                                    contentDescription = "tambah pengguna",
                                    modifier = Modifier.size((LocalTextStyle.current.fontSize.value * 1.3f).dp),
                                    tint = White
                                )
                            },
                            contentPadding = PaddingValues(
                                vertical = 8.dp,
                                horizontal = 12.dp
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
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
                        onActivateUserClick = { onActivateUser(it) },
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
                            lastPage = totalPages,
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
            "edit-user" -> UserFormDialog(
                user = it,
                onDismissRequest = dismissPopup,
                onSave = { fullName, email, password, institutionNumber, phoneNumber, role, status ->
                    selectedUser?.let { user ->
                        onEditUser(
                            UserUpdate(
                                id = user.id,
                                fullName = fullName,
                                email = email,
                                password = password,
                                npmNip = institutionNumber,
                                phoneNumber = phoneNumber,
                                role = role,
                                status = status,
                                ktmPath = user.ktmPath
                            )
                        )
                    }
                    dismissPopup()
                }
            )
            "delete-user" -> UserDeleteDialog(
                user = it,
                onDismissRequest = dismissPopup,
                onDelete = {
                    onDeleteUser(it)
                    dismissPopup()
                }
            )
            else -> {}
        }
    }
    if (popup == "add-user") {
        UserFormDialog(
            user = selectedUser,
            onDismissRequest = { dismissPopup() },
            onSave = { fullName, email, password, institutionNumber, phoneNumber, role, status ->
                password?.let { password ->
                    onAddUser(
                        UserCreate(
                            fullName = fullName,
                            email = email,
                            password = password,
                            institutionNumber = institutionNumber,
                            phoneNumber = phoneNumber,
                            role = role,
                            status = status,
                            profilePhoto = null,
                            ktmPath = null,
                            lastLoginAt = null
                        )
                    )
                }
                dismissPopup()
            }
        )
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
                                if (user.status != UserStatus.ACTIVE) ActionIconButton(
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
private fun UserFormDialog(
    user: User?,
    onDismissRequest: () -> Unit,
    // blank strings treated as null
    onSave: (
        fullName: String,
        email: String,
        password: String?,
        institutionNumber: String,
        phoneNumber: String?,
        role: UserRole,
        status: UserStatus
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by retain { mutableStateOf(user?.fullName ?: "") }
    var email by retain { mutableStateOf(user?.email ?: "") }
    var password by retain { mutableStateOf("") }
    var institutionNumber by retain { mutableStateOf(user?.institutionNumber ?: "") }
    var phoneNumber by retain { mutableStateOf(user?.phoneNumber ?: "") }
    var role by retain { mutableStateOf(user?.role ?: UserRole.STAFF) }
    var status by retain { mutableStateOf(user?.status ?: UserStatus.PENDING) }

    GlassBoxDialog(
        title = "${if (user != null) "Edit" else "Tambah"} Pengguna",
        desc = if (user != null) "Perbarui data pengguna." else "Tambah pengguna baru.",
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                UserFormFieldPair(
                    firstValue = fullName,
                    secondValue = email,
                    firstLabel = "nama lengkap",
                    secondLabel = "email",
                    firstPlaceholder = "Masukkan nama lengkap",
                    secondPlaceholder = "Masukkan email",
                    onFirstChange = { fullName = it },
                    onSecondChange = { email = it }
                )
            }
            item {
                var showPassword by retain {
                    mutableStateOf(false)
                }

                OutlinedTextField(
                    value = password ?: "",
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = buildAnnotatedString { append("PASSWORD") },
                    placeholder = if (user != null) "Kosongkan jika tidak mengubah password" else "Password minimal 8 karakter",
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    actions = {
                        Icon(
                            painter = painterResource(if (showPassword) GateTikIcons.eyeClose else GateTikIcons.eyeOpen),
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = null
                            ) { showPassword = !showPassword }
                        )
                    }
                )
            }
            item {
                UserFormFieldPair(
                    firstValue = institutionNumber,
                    secondValue = phoneNumber,
                    firstLabel = "npm/nip",
                    secondLabel = "no. telepon",
                    firstPlaceholder = "NPM/NIP",
                    secondPlaceholder = "No. Telepon",
                    onFirstChange = { institutionNumber = it },
                    onSecondChange = { phoneNumber = it }
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    UserFormSelect(
                        selectedOption = role,
                        label = "role",
                        options = UserRole.entries,
                        onOptionSelected = { role = it },
                        modifier = Modifier.weight(1f),
                        optionToString = { it.toIdString() }
                    )
                    UserFormSelect(
                        selectedOption = status,
                        label = "status",
                        options = UserStatus.entries,
                        onOptionSelected = { status = it },
                        modifier = Modifier.weight(1f),
                        optionToString = { it.capitalizedName }
                    )
                }
            }
            item {
                GradientTextButton(
                    text = "Simpan",
                    onClick = {
                        onSave(
                            fullName,
                            email,
                            password.takeIf { it.isNotBlank() },
                            institutionNumber,
                            phoneNumber.takeIf { it.isNotBlank() },
                            role,
                            status
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = fullName.isNotBlank() &&
                        email.isNotBlank() &&
                        institutionNumber.isNotBlank() &&
                        (password.length >= 8 || (user != null && password.isBlank()))
                )
            }
        }
    }
}

@Composable
private fun <T> UserFormSelect(
    selectedOption: T,
    label: String,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current,
    optionToString: (T) -> String = { it.toString() }
) {
    val labelColor by animateColorAsState(if (!isDarkMode) RoyalBlue800_50 else White50)
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = labelColor
        )
        Select(
            selectedOption = selectedOption,
            options = options,
            onOptionSelected = onOptionSelected,
            optionToString = optionToString
        )
    }
}

@Composable
private fun UserFormFieldPair(
    firstValue: String,
    secondValue: String,
    firstLabel: String,
    secondLabel: String,
    firstPlaceholder: String,
    secondPlaceholder: String,
    onFirstChange: (String) -> Unit,
    onSecondChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    firstVisualTransformation: VisualTransformation = VisualTransformation.None,
    secondVisualTransformation: VisualTransformation = VisualTransformation.None,
    firstActions: (@Composable () -> Unit)? = null,
    secondActions: (@Composable () -> Unit)? = null
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val contentModifier = Modifier
            .widthIn(min = 200.dp)
            .weight(1f)

        OutlinedTextField(
            value = firstValue,
            onValueChange = onFirstChange,
            label = buildAnnotatedString { append(firstLabel.uppercase()) },
            placeholder = firstPlaceholder,
            modifier = contentModifier,
            visualTransformation = firstVisualTransformation,
            actions = firstActions
        )
        OutlinedTextField(
            value = secondValue,
            onValueChange = onSecondChange,
            label = buildAnnotatedString { append(secondLabel.uppercase()) },
            placeholder = secondPlaceholder,
            modifier = contentModifier,
            visualTransformation = secondVisualTransformation,
            actions = secondActions
        )
    }
}

@Composable
private fun UserDeleteDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current

    GlassBoxDialog(
        title = "Hapus Pengguna",
        desc = "Apakah Anda yakin ingin menghapus ${user.fullName}?",
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(max = 400.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDarkMode) Slate900 else Gray200)
                    .clickable(onClick = onDismissRequest)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Batal",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Red500)
                    .clickable(onClick = onDelete)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Hapus",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
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
                                        imageBitmap = remoteImageLoader.loadWith(remoteImageResolver)
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


@DevicePreviews
@Composable
private fun DesktopUserManagementScreenPreview() {
    CompositionLocalProvider(
        LocalScaffoldComponentsController provides MockScaffoldComponentController
    ) {
        GateTikTheme(darkMode = isSystemInDarkTheme()) {
            Scaffold { p ->
                DesktopUserManagementScreen(
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
                    onItemsPerPageChange = {},
                    onEditUser = {},
                    onAddUser = {},
                    onDeleteUser = {},
                    onActivateUser = {}
                )
            }
        }
    }
}
