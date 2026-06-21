package kite1412.gatetik.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Gray200
import kite1412.gatetik.designsystem.theme.Gray900
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = LocalDarkMode.current,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailing: (@Composable () -> Unit)? = null
) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(GateTikIcons.search),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isDarkMode) Gray200.copy(alpha = 0.5f) else Gray900.copy(alpha = 0.5f)
                )

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = placeholder,
                    singleLine = true,
                    keyboardActions = keyboardActions
                )
            }
            trailing?.invoke()
        }
    }
}

@Composable
fun EnterIconTrailing(modifier: Modifier = Modifier) {
    GlassBox(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Icon(
            painter = painterResource(GateTikIcons.cornerDownLeft),
            contentDescription = "enter",
            modifier = Modifier.size(16.dp)
        )
    }
}