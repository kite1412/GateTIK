package kite1412.gatetik.designsystem.component

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.ui.preview.PreviewDesktopDark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun Pagination(
    currentPage: Int,
    lastPage: Int,
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
            val visiblePages = when {
                lastPage <= 3 -> (1..lastPage).toList()

                currentPage <= 2 ->
                    (1..3).toList()

                currentPage >= lastPage - 2 ->
                    ((lastPage - 2)..lastPage).toList()

                else ->
                    ((currentPage - 1)..(currentPage + 1)).toList()
            }

            val showLastPage = lastPage > visiblePages.last()

            PaginationIconButton(
                icon = GateTikIcons.chevronFirst,
                contentDescription = "pertama",
                currentPage != 1,
                onClick = { onPageChange(1) }
            )
            PaginationIconButton(
                icon = GateTikIcons.chevronRight,
                contentDescription = "sebelumnya",
                enabled = (currentPage - 1) >= 1,
                onClick = { onPageChange(currentPage - 1) },
                modifier = Modifier.rotate(180f)
            )

            visiblePages.forEach { page ->
                Page(
                    page = page,
                    selected = page == currentPage,
                    onClick = { onPageChange(it) }
                )
            }
            if (showLastPage && lastPage != 4) Text("...")
            if (showLastPage) Page(
                page = lastPage,
                selected = lastPage == currentPage,
                onClick = { onPageChange(it) }
            )

            PaginationIconButton(
                icon = GateTikIcons.chevronRight,
                contentDescription = "selanjutnya",
                enabled = (currentPage + 1) <= lastPage,
                onClick = { onPageChange(currentPage + 1) }
            )
            PaginationIconButton(
                icon = GateTikIcons.chevronLast,
                contentDescription = "akhir",
                enabled = currentPage != lastPage,
                onClick = { onPageChange(lastPage) }
            )
        }
    }
}

@Composable
private fun Page(
    page: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (selected) Blue500 else Color.Transparent
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) White else LocalContentColor.current.copy(alpha = 0.6f)
    )

    Box(
        modifier = modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable { onClick(page) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = page.toString(),
            color = textColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PaginationIconButton(
    icon: DrawableResource,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint by animateColorAsState(
        targetValue = LocalContentColor.current.copy(
            alpha = if (enabled) 1f else 0.4f
        )
    )

    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick),
        tint = tint
    )
}

@PreviewDesktopDark
@Composable
private fun PaginationPreview() {
    var currentPage by remember { mutableIntStateOf(1) }

    GateTikTheme {
        Scaffold { p ->
              Pagination(
                  currentPage = currentPage,
                  lastPage = 5,
                  onPageChange = { currentPage = it },
                  itemsPerPage = "15",
                  onItemsPerPageChange = {  },
                  modifier = Modifier.padding(p)
              )
        }
    }
}