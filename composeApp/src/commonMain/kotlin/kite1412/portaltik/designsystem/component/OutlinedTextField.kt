package kite1412.portaltik.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.theme.Black70
import kite1412.portaltik.designsystem.theme.Blue200_60
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.RoyalBlue800_50
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White15
import kite1412.portaltik.designsystem.theme.White50
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import org.jetbrains.compose.resources.painterResource

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    label: AnnotatedString? = null,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Unspecified,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null
) {
    val isDarkMode = LocalDarkMode.current
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (!isDarkMode) RoyalBlue800_50 else White50
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (!isDarkMode) Blue200_60 else White15,
                    shape = shape
                )
                .background(
                    color = White.copy(if (!isDarkMode) 0.5f else 0.04f),
                    shape = shape
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                leading?.invoke()
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = placeholder,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                    keyboardActions = keyboardActions,
                )
            }
            actions?.invoke()
        }
    }
}

@Preview
@Composable
private fun OutlineTextFieldPreview() {
    var string by remember { mutableStateOf("") }

    PortalTikTheme {
        Scaffold { p ->
              Column(
                  modifier = Modifier
                      .fillMaxSize()
                      .background(Black70),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                  OutlinedTextField(
                      value = string,
                      onValueChange = { string = it },
                      modifier = Modifier.padding(p),
                      placeholder = "Email",
                      label = buildAnnotatedString { append("EMAIL") },
                      leading = {
                          Icon(
                              painter = painterResource(PortalTikIcons.email),
                              contentDescription = "EMAIL",
                              tint = RoyalBlue800_50
                          )
                      }
                  )
                  OutlinedTextField(
                      value = string,
                      onValueChange = { string = it },
                      modifier = Modifier.padding(p),
                      placeholder = "Password",
                      leading = {
                          Icon(
                              painter = painterResource(PortalTikIcons.lock),
                              contentDescription = "lock",
                              tint = RoyalBlue800_50
                          )
                      }
                  )
              }
        }
    }
}