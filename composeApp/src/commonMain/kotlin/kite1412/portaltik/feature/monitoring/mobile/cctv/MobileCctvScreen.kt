package kite1412.portaltik.feature.monitoring.mobile.cctv

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kite1412.portaltik.CctvPlayer
import kite1412.portaltik.designsystem.component.Badge
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.component.SectionHeader
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.Red600
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White20
import kite1412.portaltik.designsystem.util.GateTikIcons
import kite1412.portaltik.model.Cctv
import kite1412.portaltik.ui.component.InfoCard
import kite1412.portaltik.ui.compositionlocal.LocalScaffoldComponentsController
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.LoadState
import kite1412.portaltik.ui.util.ScaffoldComponent
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MobileCctvScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MobileCctvViewModel = koinViewModel()
) {
    val mainCctv by viewModel.mainCctv.collectAsStateWithLifecycle()

    MobileCctvScreen(
        cctv = mainCctv,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
private fun MobileCctvScreen(
    cctv: LoadState<Cctv?>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scaffoldComponentsController = LocalScaffoldComponentsController.current
    var showInFullScreen by retain { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            contentPadding = PaddingValues(
                bottom = scaffoldComponentsController.getState(ScaffoldComponent.NAV_BAR).size.height
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SectionHeader(
                    title = "CCTV",
                    subtitle = if (cctv is LoadState.Success && cctv.data != null) cctv.data.cameraName
                    else ""
                )
            }
            item {
                CctvPlayer(
                    cctv = cctv,
                    onFullScreenClick = { showInFullScreen = !showInFullScreen }
                )
            }
            item {
                InfoCard(
                    icon = painterResource(GateTikIcons.phone),
                    title = "CCTV Monitoring",
                    description = "Sistem monitoring untuk memantau aktivitas di area gate."
                )
            }
        }
        if (showInFullScreen) Dialog(
            onDismissRequest = { showInFullScreen = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { showInFullScreen = false },
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.x),
                        contentDescription = null
                    )
                }
                CctvPlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                ) {}
            }
        }
    }
}

@Composable
private fun CctvPlayer(
    cctv: LoadState<Cctv?>,
    onFullScreenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                var showMessage by rememberSaveable { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    if (showMessage) {
                        delay(3000)
                        showMessage = false
                    }
                }
                CctvPlayer(
                    modifier = Modifier.fillMaxSize()
                ) {}
                Badge(
                    text = "LIVE",
                    containerColor = Red600,
                    contentColor = White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
                this@Column.AnimatedVisibility(
                    visible = showMessage,
                    modifier = Modifier.align(Alignment.Center),
                    exit = fadeOut(),
                    enter = fadeIn()
                ) {
                    Text(
                        text = "Pastikan terhubung dengan Wi-Fi TIK",
                        style = MaterialTheme.typography.labelSmall,
                        color = White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isCctvSuccess = cctv is LoadState.Success && cctv.data != null

                AnimatedVisibility(isCctvSuccess) {
                    val cctv = if (isCctvSuccess) cctv.data else null
                    cctv?.let {
                        Badge(
                            text = it.cameraName,
                            containerColor = White20,
                            contentColor = White
                        )
                    }
                }
                Icon(
                    painter = painterResource(GateTikIcons.zoomIn),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = onFullScreenClick
                        ),
                    tint = White
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun MobileCctvScreenPreview() {
    PortalTikTheme {
        Scaffold { p ->
              MobileCctvScreen(
                  contentPadding = p
              )
        }
    }
}
