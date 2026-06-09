package kite1412.gatetik.designsystem.component

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
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.gatetik.PlatformType
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Blue100
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.Blue900
import kite1412.gatetik.designsystem.theme.BlueIndigoGradient
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.Gray200
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Slate400
import kite1412.gatetik.designsystem.theme.Slate500
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.designsystem.theme.Slate900_95
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White30
import kite1412.gatetik.designsystem.theme.White95
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.designsystem.util.WindowWidthSize
import kite1412.gatetik.designsystem.util.rememberWindowWidthSize
import kite1412.gatetik.getPlatform
import kite1412.gatetik.ui.preview.DevicePreviews
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.Icon as ComposeIcon

@Composable
fun NavigationScaffold(
    destinations: List<Destination>,
    isDarkMode: Boolean,
    selectedDestination: Destination,
    username: String,
    userEmail: String,
    onDestinationClick: (Destination) -> Unit,
    onSignOutClick: () -> Unit,
    onDismissNavBar: () -> Unit,
    modifier: Modifier = Modifier,
    showNavigationBar: Boolean = true,
    windowWidthSize: WindowWidthSize = rememberWindowWidthSize(),
    onNavigationBarSizeChange: ((DpSize) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val sideNavigationBarEnter = fadeIn() + slideInHorizontally { -it }
    val sideNavigationBarExit = fadeOut() + slideOutHorizontally { -it }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .radialBackground()
    ) {
        content()
        AnimatedVisibility(
            visible = showNavigationBar && windowWidthSize == WindowWidthSize.MEDIUM,
            modifier = navBarSizeConsumerModifier(density, onNavigationBarSizeChange),
            enter = sideNavigationBarEnter,
            exit = sideNavigationBarExit
        ) {
            SideNavigationRail(
                destinations = destinations,
                isDarkMode = isDarkMode,
                selectedDestination = selectedDestination,
                onDestinationClick = onDestinationClick,
                onSignOutClick = onSignOutClick
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
                isDarkMode = isDarkMode,
                selectedDestination = selectedDestination,
                onDestinationClick = onDestinationClick,
                userName = username,
                userEmail = userEmail,
                onSignOutClick = onSignOutClick,
                onDismissRequest = onDismissNavBar
            )
        }

        AnimatedVisibility(
            visible = windowWidthSize == WindowWidthSize.COMPACT && showNavigationBar,
            modifier = navBarSizeConsumerModifier(density, onNavigationBarSizeChange)
                .align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            BottomNavigationBar(
                destinations = destinations,
                isDarkMode = isDarkMode,
                selectedDestination = selectedDestination,
                onDestinationClick = onDestinationClick
            )
        }
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

@Suppress("ModifierFactoryExtensionFunction")
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
private fun containerColor(isDarkMode: Boolean) = animateColorAsState(
    targetValue = if (isDarkMode) Slate900.copy(alpha = 0.2f) else White30
)

@Composable
private fun BoxScope.EndBorder(isDarkMode: Boolean) {
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.TopEnd),
        thickness = 1.dp,
        color = if (isDarkMode) Gray200.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SideNavigationRail(
    selectedDestination: Destination,
    destinations: List<Destination>,
    isDarkMode: Boolean,
    onDestinationClick: (Destination) -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by containerColor(isDarkMode)
    val onSurface = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .background(containerColor)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                destinations.forEach { destination ->
                    val selected = selectedDestination == destination
                    val iconColor by animateColorAsState(
                        targetValue = if (selected) onSurface else onSurface.copy(alpha = 0.4f)
                    )

                    if (destination.icon != null) Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = destination.label,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) { onDestinationClick(destination) },
                        tint = iconColor
                    )
                }
            }
            Icon(
                painter = painterResource(GateTikIcons.logout),
                contentDescription = "sign out",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = onSignOutClick
                    ),
                tint = Red500
            )
        }
        EndBorder(isDarkMode)
    }
}

@Composable
private fun SideNavigationDrawer(
    destinations: List<Destination>,
    isDarkMode: Boolean,
    selectedDestination: Destination,
    onDestinationClick: (Destination) -> Unit,
    userName: String,
    userEmail: String,
    onSignOutClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by containerColor(isDarkMode)

    Box(
        modifier = modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 24.dp,
                    horizontal = 16.dp
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.unila),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Column {
                        Text(
                            text = "Gate TIK",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "ACCESS CONTROL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                color = if (isDarkMode) Slate400 else Slate500
                            )
                        )
                    }
                }
                if (getPlatform().type == PlatformType.DESKTOP) GlassBox(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onDismissRequest),
                    contentPadding = PaddingValues(8.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.chevronRight),
                        contentDescription = "tutup",
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(180f),
                        tint = LocalContentColor.current
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
                        isDarkMode = isDarkMode,
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
                                color = if (isDarkMode) White else Blue900
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isDarkMode) Slate400 else Slate500
                            ),
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(GateTikIcons.logout),
                        contentDescription = "Logout",
                        tint = Red500,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onSignOutClick() }
                            .padding(4.dp)
                            .size(20.dp)
                    )
                }
            }
        }
        EndBorder(isDarkMode)
    }
}

@Composable
private fun NavigationDrawerItem(
    destination: Destination,
    selected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val selectedIconColor = if (isDarkMode) Blue100 else Blue900
    val unselectedIconColor = if (isDarkMode) Slate400 else Slate500
    
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            if (isDarkMode) Blue900.copy(alpha = 0.2f) else Blue100
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

        if (destination.icon != null) Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val bodyMedium = MaterialTheme.typography.bodyMedium

            Icon(
                painter = painterResource(destination.icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(bodyMedium.fontSize.value.dp)
            )
            Text(
                text = destination.label,
                style = bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                )
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    destinations: List<Destination>,
    isDarkMode: Boolean,
    selectedDestination: Destination,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isDarkMode) Slate900_95 else White95
    )

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor
    ) {
        destinations.forEach { destination ->
            val selected = destination == selectedDestination
            val selectedIconColor = if (isDarkMode) Blue100 else Blue900
            val unselectedIconColor = if (isDarkMode) Slate400 else Slate500

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
            icon = GateTikIcons.eyeOpen
        ),
        Destination(
            route = "eye close",
            label = "Eye Close",
            icon = GateTikIcons.eyeClose
        ),
        Destination(
            route = "lock",
            label = "Lock",
            icon = GateTikIcons.lock
        ),
        Destination(
            route = "sun",
            label = "Sun",
            icon = GateTikIcons.sun
        ),
        Destination(
            route = "moon",
            label = "Moon",
            icon = GateTikIcons.moon
        )
    )
    var selectedDestination by remember { mutableStateOf(destinations.first()) }

    GateTikTheme(darkMode = false) {
        NavigationScaffold(
            destinations = destinations,
            isDarkMode = false,
            selectedDestination = selectedDestination,
            onDestinationClick = { selectedDestination = it },
            username = "Aulia Rahman",
            userEmail = "admin@campus.edu",
            onSignOutClick = {},
            onDismissNavBar = {}
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
