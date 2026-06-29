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
import kite1412.gatetik.designsystem.component.EnterIconTrailing
import kite1412.gatetik.designsystem.component.FilterChip
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.SearchField
import kite1412.gatetik.feature.monitoring.desktop.cctv.CctvTab
import kite1412.gatetik.feature.monitoring.desktop.cctv.CctvTabSelector

@Composable
fun CctvActionBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: CctvTab? = null,
    onTabSelected: ((CctvTab) -> Unit)? = null
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
            if (selectedTab != null && onTabSelected != null) {
                CctvTabSelector(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }

            SearchField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = "Cari nama, path, atau URL...",
                modifier = Modifier.weight(1f),
                trailing = { EnterIconTrailing() }
            )
        }
    }
}
