package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import kite1412.portaltik.designsystem.theme.RoyalBlue800_30
import kite1412.portaltik.designsystem.theme.White30

@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
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
    val isDarkMode = isSystemInDarkTheme()

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
                color = if (!isDarkMode) RoyalBlue800_30 else White30
            )
            text()
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}