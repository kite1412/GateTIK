package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode

// use border if isDarkMode == false
@Composable
fun BorderedBox(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val isDarkMode = LocalDarkMode.current

    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (!isDarkMode) MaterialTheme.colorScheme.outline else Color.Transparent
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}