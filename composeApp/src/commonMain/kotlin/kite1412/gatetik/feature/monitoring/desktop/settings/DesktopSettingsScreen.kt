package kite1412.gatetik.feature.monitoring.desktop.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.gatetik.designsystem.component.GlassBoxDialog
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.component.Switch
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray200
import kite1412.gatetik.designsystem.theme.Red600
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.feature.monitoring.desktop.component.DesktopLayout
import kite1412.gatetik.feature.monitoring.desktop.util.desktopBaseModifier
import kite1412.gatetik.model.UserRole
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.compositionlocal.SnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.MockScaffoldComponentController
import kite1412.gatetik.ui.util.UiEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DesktopSettingsScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: DesktopSettingsViewModel = koinViewModel()
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val user by viewModel.signedInUser.collectAsStateWithLifecycle()
    val isPollingEnabled by viewModel.isPollingEnabled.collectAsStateWithLifecycle()
    val pollingIntervalSecond by viewModel.pollingIntervalSecond.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar)
                snackbarHostStateWrapper.showSnackbar(event.message)
        }
    }
    user?.let { user ->
        DesktopSettingsScreen(
            userRole = user.role,
            isPollingEnabled = isPollingEnabled,
            pollingIntervalSecond = pollingIntervalSecond,
            contentPadding = contentPadding,
            onThemeToggle = viewModel::updateDarkMode,
            onPollingEnabledChange = viewModel::updatePollingEnabled,
            onPollingIntervalSecondChange = viewModel::updatePollingIntervalSecond,
            modifier = modifier
        )
    }
}

@Composable
private fun DesktopSettingsScreen(
    userRole: UserRole,
    isPollingEnabled: Boolean,
    pollingIntervalSecond: Int,
    contentPadding: PaddingValues,
    onThemeToggle: (Boolean) -> Unit,
    onPollingEnabledChange: (Boolean) -> Unit,
    onPollingIntervalSecondChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    DesktopLayout(
        title = "Pengaturan",
        userRole = userRole,
        onThemeToggle = onThemeToggle,
        modifier = modifier.desktopBaseModifier()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                Polling(
                    enabled = isPollingEnabled,
                    pollingIntervalSecond = pollingIntervalSecond,
                    onEnabledChange = onPollingEnabledChange,
                    onPollingIntervalSecondChange = onPollingIntervalSecondChange
                )
            }
        }
    }
}

@Composable
private fun Polling(
    enabled: Boolean,
    pollingIntervalSecond: Int,
    onEnabledChange: (Boolean) -> Unit,
    onPollingIntervalSecondChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current
) {
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    var showPollingIntervalWarning by retain {
        mutableStateOf(false)
    }
    var interval by retain(pollingIntervalSecond) {
        mutableStateOf(pollingIntervalSecond.toString())
    }

    Section(
        title = "Polling",
        desc = "Saat diaktifkan, aplikasi akan memeriksa pembaruan terbaru secara berkala.",
        titleTrailing = {
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange
            )
        },
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = enabled
        ) {
            Section(
                title = "Polling Interval",
                desc = "Data akan diperbarui secara otomatis sesuai interval yang dipilih",
                isSubSection = true,
                titleTrailing = {
                    OutlinedTextField(
                        value = interval,
                        onValueChange = { interval = it },
                        modifier = Modifier
                            .width(120.dp)
                            .onPreviewKeyEvent { event ->
                                if (event.type == KeyEventType.KeyUp && event.key == Key.Enter) {
                                    runCatching {
                                        interval.toInt()
                                    }
                                        .getOrNull()
                                        ?.let {
                                            if (it <= 0) {
                                                snackbarHostStateWrapper.showSnackbar("Polling interval harus angka positif dan lebih dari 0")
                                                return@let true
                                            } else if (it <= 10) {
                                                showPollingIntervalWarning = true
                                                return@let true
                                            }
                                            onPollingIntervalSecondChange(it)
                                            return@let true
                                        } ?: run {
                                            snackbarHostStateWrapper.showSnackbar("Gagal memperbarui polling interval")
                                            interval = pollingIntervalSecond.toString()
                                            true
                                        }
                                }
                                false
                            },
                        singleLine = true,
                        actions = {
                            Text(
                                text = "Detik",
                                style = MaterialTheme.typography.bodySmall,
                                color = LocalContentColor.current.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            )
        }
    }
    if (showPollingIntervalWarning) {
        val dismiss = {
            interval = pollingIntervalSecond.toString()
            showPollingIntervalWarning = false
        }

        GlassBoxDialog(
            title = "Polling Interval Terlalu Pendek",
            desc = "Interval yang terlalu pendek dapat meningkatkan penggunaan baterai, jaringan, dan beban server.",
            onDismissRequest = dismiss,
            titleStyle = MaterialTheme.typography.titleMedium.copy(
                color = Red600
            )
        ) {
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    text = "Batal",
                    background = if (isDarkMode) Slate900 else Gray200,
                    onClick = dismiss
                )
                TextButton(
                    text = "Ubah",
                    background = Red600,
                    onClick = {
                        runCatching {
                            interval.toInt()
                        }
                            .getOrNull()
                            ?.let(onPollingIntervalSecondChange)
                        showPollingIntervalWarning = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TextButton(
    text: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun Section(
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    isSubSection: Boolean = false,
    titleTrailing: (@Composable () -> Unit)? = null,
    subSection: (@Composable ColumnScope.() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
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
                titleTrailing?.invoke()
            }
            if (!isSubSection) HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            subSection?.invoke(this@Column)
        }
    }
}

@DevicePreviews
@Composable
private fun DesktopSettingsScreenPreview() {
    GateTikTheme {
        Scaffold { p ->
            CompositionLocalProvider(
                LocalScaffoldComponentsController provides MockScaffoldComponentController,
                LocalSnackbarHostStateWrapper provides SnackbarHostStateWrapper()
            ) {
                DesktopSettingsScreen(
                    userRole = UserRole.ADMIN,
                    isPollingEnabled = true,
                    pollingIntervalSecond = 60,
                    contentPadding = p,
                    onThemeToggle = {},
                    onPollingEnabledChange = {},
                    onPollingIntervalSecondChange = {}
                )
            }
        }
    }
}