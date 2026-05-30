package kite1412.portaltik.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.portaltik.designsystem.theme.Blue100
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.Blue900
import kite1412.portaltik.designsystem.theme.BlueIndigoGradient
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Slate400
import kite1412.portaltik.designsystem.theme.Slate500
import kite1412.portaltik.designsystem.theme.Slate900_95
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.theme.White95
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.designsystem.util.WindowWidthSize
import kite1412.portaltik.designsystem.util.rememberWindowWidthSize
import kite1412.portaltik.ui.preview.DevicePreviews
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.Icon as ComposeIcon

@Composable
fun NavigationScaffold(
    destinations: List<Destination>,
    isDarkTheme: Boolean,
    selectedDestination: Destination,
    username: String,
    userEmail: String,
    onDestinationClick: (Destination) -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
    showNavigationBar: Boolean = true,
    windowWidthSize: WindowWidthSize = rememberWindowWidthSize(),
    onNavigationBarSizeChange: ((DpSize) -> Unit)? = null,
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
    val density = LocalDensity.current
    val sideNavigationBarEnter = fadeIn() + slideInHorizontally { -it }
    val sideNavigationBarExit = fadeOut() + slideOutHorizontally { -it }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(
            visible = showNavigationBar && windowWidthSize == WindowWidthSize.MEDIUM,
            modifier = navBarSizeConsumerModifier(density, onNavigationBarSizeChange),
            enter = sideNavigationBarEnter,
            exit = sideNavigationBarExit
        ) {
            SideNavigationRail(
                destinations = destinations,
                isDarkTheme = isDarkTheme,
                selectedDestination = selectedDestination,
                onDestinationClick = onDestinationClick
            )
        }

        AnimatedVisibility(
            visible = showNavigationBar && windowWidthSize == WindowWidthSize.LARGE,
            modifier = navBarSizeConsumerModifier(density, onNavigationBarSizeChange),
            enter = sideNavigationBarEnter,
            exit = sideNavigationBarExit
        ) {
            SideNavigationDrawer(
                destinations = destinations,
                isDarkTheme = isDarkTheme,
                selectedDestination = selectedDestination,
                onDestinationClick = onDestinationClick,
                userName = username,
                userEmail = userEmail,
                onLogoutClick = onSignOutClick
            )
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            bottomBar = if (windowWidthSize == WindowWidthSize.COMPACT) {
                {
                    AnimatedVisibility(
                        visible = showNavigationBar,
                        modifier = navBarSizeConsumerModifier(density, onNavigationBarSizeChange),
                        enter = fadeIn() + slideInVertically { it },
                        exit = fadeOut() + slideOutVertically { it }
                    ) {
                        BottomNavigationBar(
                            destinations = destinations,
                            isDarkTheme = isDarkTheme,
                            selectedDestination = selectedDestination,
                            onDestinationClick = onDestinationClick
                        )
                    }
                }
            } else {
                {}
            }
        ) { p -> content(p) }
    }
}

data class Destination(
    val route: String,
    val label: String,
    val icon: DrawableResource?
) {
    companion object {
        val Empty = Destination("", "", null)
    }
}

private fun navBarSizeConsumerModifier(density: Density, consumer: ((DpSize) -> Unit)?): Modifier =
    if (consumer != null) Modifier.onGloballyPositioned { coordinates ->
        consumer(
            with(density) {
                DpSize(
                    width = coordinates.size.width.toDp(),
                    height = coordinates.size.height.toDp()
                )
            }
        )
    } else Modifier

@Composable
private fun SideNavigationRail(
    destinations: List<Destination>,
    isDarkTheme: Boolean,
    selectedDestination: Destination,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isDarkTheme) Slate900_95 else White60
    )

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = containerColor,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Icon(
                    painter = painterResource(PortalTikIcons.unila),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    ) {
        destinations.forEach { destination ->
            val selected = destination == selectedDestination
            val selectedIconColor = if (isDarkTheme) Blue100 else Blue900
            val unselectedIconColor = if (isDarkTheme) Slate400 else Slate500

            NavigationRailItem(
                selected = selected,
                onClick = { onDestinationClick(destination) },
                icon = {
                    if (destination.icon != null) ComposeIcon(
                        painter = painterResource(destination.icon),
                        contentDescription = destination.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    selectedTextColor = selectedIconColor,
                    unselectedTextColor = unselectedIconColor,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun SideNavigationDrawer(
    destinations: List<Destination>,
    isDarkTheme: Boolean,
    selectedDestination: Destination,
    onDestinationClick: (Destination) -> Unit,
    userName: String,
    userEmail: String,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isDarkTheme) Slate900_95 else White60
    )

    Surface(
        modifier = modifier
            .width(280.dp)
            .fillMaxHeight(),
        color = containerColor,
        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(PortalTikIcons.unila),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Column {
                    Text(
                        text = "Portal TIK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "ACCESS CONTROL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp,
                            color = if (isDarkTheme) Slate400 else Slate500
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                destinations.forEach { destination ->
                    val selected = destination == selectedDestination
                    NavigationDrawerItem(
                        destination = destination,
                        selected = selected,
                        isDarkTheme = isDarkTheme,
                        onClick = { onDestinationClick(destination) }
                    )
                }
            }

            GlassBox(
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                brush = Brush.linearGradient(BlueIndigoGradient),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) White else Blue900
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isDarkTheme) Slate400 else Slate500
                            ),
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ComposeIcon(
                        painter = painterResource(PortalTikIcons.logout),
                        contentDescription = "Logout",
                        tint = if (isDarkTheme) Slate400 else Slate500,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onLogoutClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationDrawerItem(
    destination: Destination,
    selected: Boolean,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val selectedIconColor = if (isDarkTheme) Blue100 else Blue900
    val unselectedIconColor = if (isDarkTheme) Slate400 else Slate500
    
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            if (isDarkTheme) Blue900.copy(alpha = 0.2f) else Blue100
        } else Color.Transparent
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (selected) selectedIconColor else unselectedIconColor
    )
    
    val indicatorWidth by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(indicatorWidth)
                .height(24.dp)
                .background(Blue500, RoundedCornerShape(2.dp))
        )
        
        if (selected) {
            Spacer(modifier = Modifier.width(12.dp))
        } else {
            val spacerWidth by animateDpAsState(targetValue = if (selected) 12.dp else 0.dp)
            Spacer(modifier = Modifier.width(spacerWidth))
        }

        if (destination.icon != null) ComposeIcon(
            painter = painterResource(destination.icon),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = destination.label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        )
    }
}

@Composable
private fun BottomNavigationBar(
    destinations: List<Destination>,
    isDarkTheme: Boolean,
    selectedDestination: Destination,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isDarkTheme) Slate900_95 else White95
    )

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor
    ) {
        destinations.forEach { destination ->
            val selected = destination == selectedDestination
            val selectedIconColor = if (isDarkTheme) Blue100 else Blue900
            val unselectedIconColor = if (isDarkTheme) Slate400 else Slate500

            NavigationBarItem(
                selected = selected,
                onClick = { onDestinationClick(destination) },
                icon = {
                    if (destination.icon != null) ComposeIcon(
                        painter = painterResource(destination.icon),
                        contentDescription = destination.label,
                        modifier = Modifier.size(18.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) selectedIconColor else unselectedIconColor
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@DevicePreviews
@Composable
private fun NavigationScaffoldPreviewContent() {
    val destinations = listOf(
        Destination(
            route = "eye open",
            label = "Eye Open",
            icon = PortalTikIcons.eyeOpen
        ),
        Destination(
            route = "eye close",
            label = "Eye Close",
            icon = PortalTikIcons.eyeClose
        ),
        Destination(
            route = "lock",
            label = "Lock",
            icon = PortalTikIcons.lock
        ),
        Destination(
            route = "sun",
            label = "Sun",
            icon = PortalTikIcons.sun
        ),
        Destination(
            route = "moon",
            label = "Moon",
            icon = PortalTikIcons.moon
        )
    )
    var selectedDestination by remember { mutableStateOf(destinations.first()) }

    PortalTikTheme(darkMode = false) {
        NavigationScaffold(
            destinations = destinations,
            isDarkTheme = false,
            selectedDestination = selectedDestination,
            onDestinationClick = { selectedDestination = it },
            username = "Aulia Rahman",
            userEmail = "admin@campus.edu",
            onSignOutClick = {}
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Content")
            }
        }
    }
}
