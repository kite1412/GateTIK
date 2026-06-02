package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.White20
import kite1412.portaltik.ui.preview.PreviewPhoneDark

@Composable
fun <T> Table(
    columns: List<TableColumn<T>>,
    items: List<T>,
    modifier: Modifier = Modifier,
    headerBackgroundColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f)
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBackgroundColor)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                columns.forEach { column ->
                    Box(modifier = Modifier.weight(column.weight)) {
                        Text(
                            text = column.title.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Rows
            items.forEach { item ->
                Column {
                    HorizontalDivider(color = White20)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        columns.forEach { column ->
                            Box(modifier = Modifier.weight(column.weight)) {
                                column.content(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TableColumn<T>(
    val title: String,
    val weight: Float = 1f,
    val content: @Composable (T) -> Unit
)

@Preview
@PreviewPhoneDark
@Composable
private fun TablePreview() {
    val items = listOf("Apple", "Banana", "Cherry")
    val columns = listOf<TableColumn<String>>(
        TableColumn(title = "Name", weight = 1f) { Text(it) },
        TableColumn(title = "Length", weight = 1f) { Text(it.length.toString()) }
    )
    PortalTikTheme(darkMode = isSystemInDarkTheme()) {
        Scaffold { p ->
            Table(
                columns = columns,
                items = items,
                modifier = Modifier.padding(p)
            )
        }
    }
}
