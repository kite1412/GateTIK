package kite1412.portaltik.feature.admin.mobile.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.component.PrimaryButton
import kite1412.portaltik.designsystem.component.SectionHeader
import kite1412.portaltik.designsystem.component.Switch
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red600
import kite1412.portaltik.designsystem.theme.RoyalBlueIndigoGradient
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.component.SettingsDivider
import kite1412.portaltik.ui.component.SettingsGroup
import kite1412.portaltik.ui.component.SettingsItem
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.ScaffoldComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileAdminProfileScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileAdminProfileViewModel = koinViewModel()
) {
    MobileAdminProfileScreen(
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileAdminProfileScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    val scaffoldComponentsController = LocalScaffoldComponentsController.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(
            bottom = scaffoldComponentsController.getState(ScaffoldComponent.NAV_BAR).size.height
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionHeader(
                title = "Profil",
                subtitle = ""
            )
        }

        item {
            ProfileHeaderCard(
                name = "Aulia Rahman",
                email = "admin@campus.edu",
                role = "ADMIN"
            )
        }

        item {
            SettingsGroup(title = "AKUN") {
                SettingsItem(
                    icon = painterResource(PortalTikIcons.idCard),
                    title = "NPM / NIP",
                    subtitle = "ADM-001",
                    trailing = {
                        Icon(
                            painter = painterResource(PortalTikIcons.chevronRight),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                SettingsDivider()
                SettingsItem(
                    icon = painterResource(PortalTikIcons.email),
                    title = "EMAIL",
                    subtitle = "admin@campus.edu",
                    trailing = {
                        Icon(
                            painter = painterResource(PortalTikIcons.chevronRight),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                SettingsDivider()
                SettingsItem(
                    icon = painterResource(PortalTikIcons.shield),
                    title = "ROLE",
                    subtitle = "admin",
                    trailing = {
                        Icon(
                            painter = painterResource(PortalTikIcons.chevronRight),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        item {
            SettingsGroup(title = "PENGATURAN") {
                SettingsItem(
                    icon = painterResource(if (isDarkMode) PortalTikIcons.moon else PortalTikIcons.sun),
                    subtitle = "Mode gelap",
                    trailing = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { /* Toggle dark mode logic */ },
                            isDarkMode = isDarkMode
                        )
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = "Keluar",
                onClick = { /* Logout logic */ },
                containerColor = Red600,
                leading = {
                    Icon(
                        painter = painterResource(PortalTikIcons.logout),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Portal TIK - v1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    name: String,
    email: String,
    role: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(RoyalBlueIndigoGradient))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.toString() ?: "",
                style = MaterialTheme.typography.headlineSmall,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                color = White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Badge(
                text = role,
                containerColor = White.copy(alpha = 0.2f),
                contentColor = White
            )
        }
    }
}

@DevicePreviews
@Composable
private fun MobileAdminProfileScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              MobileAdminProfileScreen(
                  contentPadding = p,
                  modifier = Modifier.padding(p)
              )
        }
    }
}
