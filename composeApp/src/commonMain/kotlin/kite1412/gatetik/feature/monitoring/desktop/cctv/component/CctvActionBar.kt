package kite1412.gatetik.feature.monitoring.desktop.cctv.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.component.FilterChip
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.feature.monitoring.desktop.cctv.CctvTab
import kite1412.gatetik.feature.monitoring.desktop.cctv.CctvTabSelector

@Composable
fun CctvActionBar(
    selectedTab: CctvTab,
    onTabSelected: (CctvTab) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    gridColumns: Int,
    onGridColumnsChange: (Int) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassBox(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CctvTabSelector(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )

            SearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Cari nama, path, atau URL...",
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(2, 3, 4).forEach { cols ->
                    FilterChip(
                        text = cols.toString(),
                        isSelected = gridColumns == cols,
                        onClick = { onGridColumnsChange(cols) },
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }
            }
        }
    }
}
