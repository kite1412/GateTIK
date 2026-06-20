package kite1412.gatetik.feature.monitoring.desktop.cctv.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.FilterChip
import kite1412.gatetik.designsystem.component.GlassBoxDialog
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.component.PrimaryButton
import kite1412.gatetik.designsystem.theme.Red500
import kite1412.gatetik.designsystem.theme.Slate500
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.model.Cctv
import kite1412.gatetik.model.CctvType
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddCctvDialog(
    camera: Cctv?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, path: String, url: String, type: CctvType) -> Unit
) {
    var name by remember { mutableStateOf(camera?.cameraName ?: "") }
    var path by remember { mutableStateOf(camera?.path ?: "") }
    var url by remember { mutableStateOf(camera?.streamUrl ?: "") }
    var type by remember { mutableStateOf(camera?.type ?: CctvType.MONITOR) }

    GlassBoxDialog(
        title = if (camera == null) "Tambah Kamera" else "Edit Kamera",
        desc = if (camera == null) "Tambah kamera baru ke sistem" else "Ubah detail kamera",
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = buildAnnotatedString { append("CAMERA NAME") },
                placeholder = "e.g. Main Gate Camera"
            )
            OutlinedTextField(
                value = path,
                onValueChange = { path = it },
                label = buildAnnotatedString { append("PATH") },
                placeholder = "e.g. /gate1"
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = buildAnnotatedString { append("STREAM URL") },
                placeholder = "rtsp://... atau http://...",
                leading = {
                    Icon(
                        painter = painterResource(GateTikIcons.wifi),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "TIPE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        text = "Monitor",
                        isSelected = type == CctvType.MONITOR,
                        onClick = { type = CctvType.MONITOR }
                    )
                    FilterChip(
                        text = "Interkom",
                        isSelected = type == CctvType.INTERCOM,
                        onClick = { type = CctvType.INTERCOM }
                    )
                }
            }

            PrimaryButton(
                text = if (camera == null) "Tambah Kamera" else "Simpan Perubahan",
                onClick = { onConfirm(name, path, url, type) },
                leading = {
                    Icon(
                        painter = painterResource(GateTikIcons.userCheck), // Reusing check icon
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                }
            )
        }
    }
}

@Composable
fun DeleteCctvDialog(
    camera: Cctv,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    GlassBoxDialog(
        title = "Hapus Kamera",
        desc = "Yakin ingin menghapus ${camera.cameraName}?",
        onDismissRequest = onDismiss
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
        ) {
            PrimaryButton(
                text = "Batal",
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                containerColor = Slate500
            )
            PrimaryButton(
                text = "Hapus",
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                containerColor = Red500,
                leading = {
                    Icon(
                        painter = painterResource(GateTikIcons.trash),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = White
                    )
                }
            )
        }
    }
}
