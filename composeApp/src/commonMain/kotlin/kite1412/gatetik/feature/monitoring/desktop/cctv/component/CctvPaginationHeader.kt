package kite1412.gatetik.feature.monitoring.desktop.cctv.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.StatusIndicator
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.ui.component.ActionIconButton

@Composable
fun CctvPaginationHeader(
    currentPage: Int,
    totalPages: Int,
    startIndex: Int,
    endIndex: Int,
    totalItems: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Menampilkan kamera ${if (totalItems > 0) startIndex + 1 else 0}-${endIndex} dari ${totalItems}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Page Indicators
            if (totalPages > 1) {
                repeat(totalPages) { page ->
                    StatusIndicator(
                        color = if (page == currentPage) Blue500 else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier
                            .size(6.dp)
                            .clickable { onPageChange(page) }
                    )
                }

                Spacer(Modifier.width(8.dp))

                ActionIconButton(
                    icon = GateTikIcons.chevronRight,
                    onClick = { if (currentPage > 0) onPageChange(currentPage - 1) },
                    modifier = Modifier.rotate(180f),
                    enabled = currentPage > 0,
                    tint = if (currentPage > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )

                ActionIconButton(
                    icon = GateTikIcons.chevronRight,
                    onClick = { if (currentPage < totalPages - 1) onPageChange(currentPage + 1) },
                    enabled = currentPage < totalPages - 1,
                    tint = if (currentPage < totalPages - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
