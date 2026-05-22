package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.PortalTikTheme

@Composable
fun OutlinedTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        leading?.invoke()
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun OutlinedTextButtonPreview() {
    PortalTikTheme {
        Scaffold { p ->
              OutlinedTextButton(
                  text = "A button",
                  onClick = {},
                  modifier = Modifier.padding(p)
              )
        }
    }
}