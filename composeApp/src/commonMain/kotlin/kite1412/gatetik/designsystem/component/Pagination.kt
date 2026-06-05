package kite1412.gatetik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Pagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    itemsPerPage: String,
    onItemsPerPageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Per Halaman",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(LocalContentColor.current.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.widthIn(max = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BasicTextField(
                        value = itemsPerPage,
                        onValueChange = onItemsPerPageChange,
                        singleLine = true,
                        keyboardType = KeyboardType.Number
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaginationIconButton(
                icon = GateTikIcons.chevronFirst,
                contentDescription = "pertama",
                onClick = {}
            )
            PaginationIconButton(
                icon = GateTikIcons.chevronRight,
                contentDescription = "sebelumnya",
                onClick = {},
                modifier = Modifier.rotate(180f)
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Blue500)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentPage.toString(),
                    color = White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            PaginationIconButton(
                icon = GateTikIcons.chevronRight,
                contentDescription = "selanjutnya",
                onClick = {}
            )
            PaginationIconButton(
                icon = GateTikIcons.chevronLast,
                contentDescription = "akhir",
                onClick = {}
            )
        }
    }
}

@Composable
private fun PaginationIconButton(
    icon: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.clickable(
            indication = null,
            interactionSource = null,
            onClick = onClick
        ),
        tint = LocalContentColor.current.copy(alpha = 0.4f)
    )
}