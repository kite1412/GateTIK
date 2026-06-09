package kite1412.gatetik.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import kite1412.gatetik.designsystem.theme.RoyalBlue800_30
import kite1412.gatetik.designsystem.theme.White30
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode

@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String? = null,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Unspecified,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val contentColor = LocalContentColor.current
    val textStyle = LocalTextStyle.current.copy(
        color = contentColor
    )
    val cursorColor = MaterialTheme.colorScheme.onPrimary
    val isDarkMode = LocalDarkMode.current
    val placeholderColor by animateColorAsState(if (!isDarkMode) RoyalBlue800_30 else White30)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        singleLine = singleLine,
        decorationBox = { text ->
            if (placeholder != null && value.isEmpty()) Text(
                text = placeholder,
                color = placeholderColor
            )
            text()
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        enabled = enabled
    )
}