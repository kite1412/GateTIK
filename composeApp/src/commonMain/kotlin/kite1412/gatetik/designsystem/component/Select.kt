package kite1412.gatetik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Gray200
import kite1412.gatetik.designsystem.theme.Slate900
import kite1412.gatetik.designsystem.theme.White60
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> Select(
    selectedOption: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    isDarkMode: Boolean = LocalDarkMode.current,
    optionToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold
            )
        }

        Box {
            val dropdownContainerColor by animateColorAsState(
                if (isDarkMode) Slate900.copy(alpha = 0.6f) else White60
            )
            val dropdownBorderColor by animateColorAsState(
                if (isDarkMode) Gray200.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary
            )

            GlassBox(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = optionToString(selectedOption),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(GateTikIcons.chevronRight),
                        contentDescription = null,
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(90f),
                        tint = LocalContentColor.current.copy(alpha = 0.5f)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = dropdownContainerColor,
                border = BorderStroke(
                    width = 1.dp,
                    color = dropdownBorderColor
                ),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 0.dp,
                offset = DpOffset(x = 0.dp, y = 4.dp)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionToString(option),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
