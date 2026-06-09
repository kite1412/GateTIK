package kite1412.gatetik.feature.shared.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.GradientTextButton
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.component.PrimaryButton
import kite1412.gatetik.designsystem.component.SectionHeader
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Emerald600
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray400
import kite1412.gatetik.designsystem.theme.Indigo600
import kite1412.gatetik.designsystem.theme.Red600
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.designsystem.util.WindowWidthSize
import kite1412.gatetik.designsystem.util.rememberWindowWidthSize
import kite1412.gatetik.model.User
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.model.UserStatus
import kite1412.gatetik.ui.component.ThemeToggle
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.LoadingState
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.navBarPadding
import kite1412.gatetik.util.timestampString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    useDefaultHeader: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val userLoadState by viewModel.user.collectAsStateWithLifecycle(LoadState.Loading())

    ProfileScreen(
        userLoadState = userLoadState,
        useDefaultHeader = useDefaultHeader,
        contentPadding = contentPadding,
        onDarkModeToggle = viewModel::setDarkMode,
        onLogout = viewModel::logout,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    userLoadState: LoadState<User>,
    useDefaultHeader: Boolean,
    contentPadding: PaddingValues,
    onDarkModeToggle: (Boolean) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    consumeNavBarSize: Boolean = true,
    applyContentPaddingOnScrollableContainer: Boolean = false
) {
    val windowSize = rememberWindowWidthSize()
    val darkMode = LocalDarkMode.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .run {
                if (!applyContentPaddingOnScrollableContainer) padding(contentPadding)
                else this
            }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = with(if (consumeNavBarSize) navBarPadding() else PaddingValues(0.dp)) {
                if (!applyContentPaddingOnScrollableContainer) this
                else PaddingValues(
                    start = calculateStartPadding(LayoutDirection.Ltr) +
                        contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = calculateEndPadding(LayoutDirection.Ltr) +
                        contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                    top = calculateTopPadding() + contentPadding.calculateTopPadding(),
                    bottom = calculateBottomPadding() + contentPadding.calculateBottomPadding()
                )
            },
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (useDefaultHeader) item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SectionHeader(
                        title = "Profil",
                        subtitle = "Kelola informasi profil dan pengaturan akun Anda"
                    )
                    Row {
                        Spacer(Modifier.width(16.dp))
                        ThemeToggle(
                            onToggle = { onDarkModeToggle(!darkMode) }
                        )
                    }
                }
            }

            if (userLoadState is LoadState.Success) {
                val user = userLoadState.data

                item {
                    if (windowSize == WindowWidthSize.COMPACT) {
                        ProfileCompactLayout(user, onLogout)
                    } else {
                        ProfileWideLayout(user, onLogout)
                    }
                }
            }
        }

        if (userLoadState is LoadState.Loading) LoadingState(
            message = userLoadState.message
        )
    }
}

@Composable
private fun ProfileCompactLayout(
    user: User,
    onLogout: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        BaseInfoSection(user)
        ChangePasswordSection()
        AccountInfoSection(user)
        LogoutButton(onLogout)
    }
}

@Composable
private fun ProfileWideLayout(
    user: User,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1.8f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BaseInfoSection(user)
            ChangePasswordSection()
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AccountInfoSection(user)
            LogoutButton(onLogout)
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    subtitle: String,
    icon: Painter,
    iconColor: Color,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassBox(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = iconColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                trailing?.invoke()
            }
            content()
        }
    }
}

@Composable
private fun BaseInfoSection(user: User) {
    var isEditing by remember { mutableStateOf(false) }
    var fullName by remember(user) { mutableStateOf(user.fullName) }
    var email by remember(user) { mutableStateOf(user.email) }
    var institutionNumber by remember(user) { mutableStateOf(user.institutionNumber) }
    var phoneNumber by remember(user) { mutableStateOf(user.phoneNumber ?: "") }

    ProfileSection(
        title = "Informasi Dasar",
        subtitle = "Detail data pribadi",
        icon = painterResource(GateTikIcons.person),
        iconColor = Blue500,
        trailing = {
            if (!isEditing) {
                Text(
                    text = "Ubah Profil",
                    color = Blue500,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Blue500.copy(alpha = 0.1f))
                        .clickable { isEditing = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            } else {
                Text(
                    text = "Batal",
                    color = Red600,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Red600.copy(alpha = 0.1f))
                        .clickable {
                            isEditing = false
                            fullName = user.fullName
                            email = user.email
                            institutionNumber = user.institutionNumber
                            phoneNumber = user.phoneNumber ?: ""
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ProfileTextField(
                label = "Nama Lengkap",
                value = fullName,
                onValueChange = { fullName = it },
                enabled = isEditing,
                leadingIcon = GateTikIcons.person,
                required = true
            )
            ProfileTextField(
                label = "Alamat Email",
                value = email,
                onValueChange = { email = it },
                enabled = isEditing,
                leadingIcon = GateTikIcons.email,
                required = true
            )
            ProfileTextField(
                label = "NPM/NIP",
                value = institutionNumber,
                onValueChange = { institutionNumber = it },
                enabled = isEditing,
                leadingIcon = GateTikIcons.idCard,
                required = true
            )
            ProfileTextField(
                label = "Nomor Telepon",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                enabled = isEditing,
                leadingIcon = GateTikIcons.phone,
                placeholder = "Belum diatur"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileTextField(
                    label = "Role",
                    value = user.role.toIdString(),
                    onValueChange = {},
                    enabled = false,
                    leadingIcon = GateTikIcons.shield,
                    modifier = Modifier.weight(1f)
                )
                ProfileTextField(
                    label = "Status",
                    value = user.status.capitalizedName,
                    onValueChange = {},
                    enabled = false,
                    leadingIcon = GateTikIcons.userCheck,
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedVisibility(visible = isEditing) {
                PrimaryButton(
                    text = "Simpan Perubahan",
                    onClick = { isEditing = false },
                    containerColor = Indigo600,
                    leading = {
                        Icon(
                            painter = painterResource(GateTikIcons.userPen),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    enabled = fullName.isNotBlank() &&
                            email.isNotBlank() &&
                            institutionNumber.isNotBlank()
                )
            }
        }
    }
}

@Composable
private fun ChangePasswordSection() {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val enabled = currentPassword.isNotBlank() &&
            newPassword.length >= 8 &&
            newPassword == confirmPassword

    ProfileSection(
        title = "Ubah Kata Sandi",
        subtitle = "Perbarui kata sandi untuk menjaga keamanan akun",
        icon = painterResource(GateTikIcons.lock),
        iconColor = Indigo600
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ProfileTextField(
                label = "Kata Sandi Saat Ini",
                value = currentPassword,
                onValueChange = { currentPassword = it },
                placeholder = "Masukkan kata sandi saat ini",
                leadingIcon = GateTikIcons.lock,
                required = true,
                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailing = {
                    PasswordVisibilityToggle(
                        visible = currentPasswordVisible,
                        onToggle = { currentPasswordVisible = !currentPasswordVisible }
                    )
                }
            )
            ProfileTextField(
                label = "Kata Sandi Baru",
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = "Masukkan kata sandi baru (min. 8 karakter)",
                leadingIcon = GateTikIcons.lock,
                required = true,
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailing = {
                    PasswordVisibilityToggle(
                        visible = newPasswordVisible,
                        onToggle = { newPasswordVisible = !newPasswordVisible }
                    )
                }
            )
            ProfileTextField(
                label = "Konfirmasi Kata Sandi Baru",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Masukkan ulang kata sandi baru",
                leadingIcon = GateTikIcons.lock,
                required = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailing = {
                    PasswordVisibilityToggle(
                        visible = confirmPasswordVisible,
                        onToggle = { confirmPasswordVisible = !confirmPasswordVisible }
                    )
                }
            )
            GradientTextButton(
                text = "Perbarui Kata Sandi",
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                leading = {
                    Icon(
                        painter = painterResource(GateTikIcons.lock),
                        contentDescription = null,
                        tint = if (enabled) Color.White else Gray400.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                enabled = enabled
            )
        }
    }
}

@Composable
private fun AccountInfoSection(user: User) {
    ProfileSection(
        title = "Info Akun",
        subtitle = "",
        icon = painterResource(GateTikIcons.scrollText),
        iconColor = Emerald600
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AccountInfoItem(
                label = "Login Terakhir",
                value = user.lastLoginAt?.timestampString ?: "Belum pernah login"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            AccountInfoItem(
                label = "Akun Dibuat",
                value = user.createdAt.timestampString
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            AccountInfoItem(
                label = "Terakhir Diperbarui",
                value = user.updatedAt?.timestampString ?: "-"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            AccountInfoItem(
                label = "ID Pengguna",
                value = "#${user.id.toString().padStart(6, '0')}"
            )
        }
    }
}

@Composable
private fun AccountInfoItem(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    leadingIcon: DrawableResource? = null,
    required: Boolean = false,
    isDarkMode: Boolean = LocalDarkMode.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null
) {
    val labelText = if (required) {
        buildAnnotatedString {
            append(label)
            append(" ")
            withStyle(SpanStyle(color = Red600)) {
                append("*")
            }
        }
    } else {
        buildAnnotatedString { append(label) }
    }
    val textColor by animateColorAsState(
        targetValue = if (enabled) LocalContentColor.current else Gray400
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        textColor = textColor,
        disabledBackgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        label = labelText,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        leading = leadingIcon?.let {
            {
                Icon(
                    painter = painterResource(it),
                    contentDescription = null,
                    tint = if (isDarkMode) MaterialTheme.colorScheme.primary else Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        actions = if (enabled) trailing else null
    )
}

@Composable
private fun PasswordVisibilityToggle(
    visible: Boolean,
    onToggle: () -> Unit
) {
    Icon(
        painter = painterResource(if (visible) GateTikIcons.eyeOpen else GateTikIcons.eyeClose),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .size(20.dp)
            .clickable(onClick = onToggle)
    )
}

@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    PrimaryButton(
        text = "Keluar",
        onClick = onLogout,
        containerColor = Red600,
        leading = {
            Icon(
                painter = painterResource(GateTikIcons.logout),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}

@DevicePreviews
@Composable
private fun ProfileScreenPreview() {
    val mockUser = User(
        id = 1,
        fullName = "Super Admin",
        email = "admin@example.com",
        role = UserRole.ADMIN,
        status = UserStatus.ACTIVE,
        institutionNumber = "ADM001",
        phoneNumber = "081234567890",
        createdAt = kite1412.gatetik.util.now(),
        updatedAt = kite1412.gatetik.util.now(),
        lastLoginAt = kite1412.gatetik.util.now()
    )

    GateTikTheme(darkMode = isSystemInDarkTheme()) {
        Scaffold { p ->
            CompositionLocalProvider(
                LocalScaffoldComponentsController provides MockScaffoldComponentController,
                LocalDarkMode provides isSystemInDarkTheme()
            ) {
                ProfileScreen(
                    useDefaultHeader = true,
                    userLoadState = LoadState.Success(mockUser),
                    contentPadding = p,
                    onDarkModeToggle = {},
                    onLogout = {}
                )
            }
        }
    }
}
