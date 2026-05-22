package kite1412.portaltik.feature.shared.authentication

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kite1412.portaltik.designsystem.component.GlassBox
import kite1412.portaltik.designsystem.component.GradientTextButton
import kite1412.portaltik.designsystem.component.Icon
import kite1412.portaltik.designsystem.component.OutlinedTextField
import kite1412.portaltik.designsystem.extension.radialBackground
import kite1412.portaltik.designsystem.theme.Blue200_60
import kite1412.portaltik.designsystem.theme.Blue300
import kite1412.portaltik.designsystem.theme.Blue500
import kite1412.portaltik.designsystem.theme.BlueSlateGradient
import kite1412.portaltik.designsystem.theme.LighterBlueLightBlueGradient
import kite1412.portaltik.designsystem.theme.PortalTikTheme
import kite1412.portaltik.designsystem.theme.RoyalBlue800_50
import kite1412.portaltik.designsystem.theme.RoyalBlue800_60
import kite1412.portaltik.designsystem.theme.White
import kite1412.portaltik.designsystem.theme.White10
import kite1412.portaltik.designsystem.theme.White15
import kite1412.portaltik.designsystem.theme.White50
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.preview.DevicePreviews
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: AuthenticationViewModel = koinViewModel()
) {
    val email = viewModel.email
    val password = viewModel.password
    val isDarkMode = LocalDarkMode.current

    AuthenticationScreen(
        email = email,
        password = password,
        contentPadding = contentPadding,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignIn = viewModel::signIn,
        onRegisterClick = {},
        onToggleDarkMode = { viewModel.toggleDarkMode(isDarkMode) },
        modifier = modifier
    )
}

@Composable
private fun AuthenticationScreen(
    email: String,
    password: String,
    contentPadding: PaddingValues,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: (email: String, password: String) -> Unit,
    onRegisterClick: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    val gradientColor1 by animateColorAsState(if (isDarkMode) BlueSlateGradient[0] else LighterBlueLightBlueGradient[0])
    val gradientColor2 by animateColorAsState(if (isDarkMode) BlueSlateGradient[1] else LighterBlueLightBlueGradient[1])
    val backgroundBrush = Brush.verticalGradient(listOf(gradientColor1, gradientColor2))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .run {
                if (isDarkMode) radialBackground()
                else this
            }
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val toggleBorderColor by animateColorAsState(if (isDarkMode) White15 else Blue200_60)
        val toggleBackgroundColor by animateColorAsState(if (isDarkMode) White10 else White)
        val toggleIconColor by animateColorAsState(if (isDarkMode) White else Blue500)

        GlassBox(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 32.dp
            )
        ) {
            val titleColor by animateColorAsState(if (isDarkMode) White else MaterialTheme.colorScheme.onSurface)
            val subtitleColor by animateColorAsState(if (isDarkMode) White60 else RoyalBlue800_60)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(PortalTikIcons.unila),
                        contentDescription = "App Icon",
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Portal TIK",
                        style = MaterialTheme.typography.titleMedium,
                        color = titleColor
                    )
                    Text(
                        text = "Sistem Smart Gate TIK UNILA",
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val iconColor by animateColorAsState(if (isDarkMode) White50 else RoyalBlue800_50)
                    var showPassword by retain { mutableStateOf(false) }

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = buildAnnotatedString { append("EMAIL KAMPUS") },
                        placeholder = "@students.unila.ac.id",
                        singleLine = true,
                        keyboardType = KeyboardType.Email,
                        leading = {
                            Icon(
                                painter = painterResource(PortalTikIcons.email),
                                contentDescription = "Email Icon",
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = buildAnnotatedString { append("KATA SANDI") },
                        placeholder = "kata sandi",
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password,
                        leading = {
                            Icon(
                                painter = painterResource(PortalTikIcons.lock),
                                contentDescription = "Lock Icon",
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        actions = {
                            Icon(
                                painter = painterResource(
                                    resource = if (showPassword) PortalTikIcons.eyeClose else PortalTikIcons.eyeOpen
                                ),
                                contentDescription = if (showPassword) "sembunyikan password" else "tampilkan password",
                                modifier = Modifier.clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    showPassword = !showPassword
                                },
                                tint = iconColor
                            )
                        }
                    )
                }
                GradientTextButton(
                    text = "Masuk",
                    onClick = {
                        onSignIn(email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank()
                )
                val secondaryTextColor by animateColorAsState(if (isDarkMode) White50 else MaterialTheme.colorScheme.onSurfaceVariant)
                val linkColor by animateColorAsState(if (!isDarkMode) Blue500 else Blue300)

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = secondaryTextColor
                            )
                        ) {
                            append("Belum punya akun? ")
                        }
                        withLink(
                            link = LinkAnnotation.Clickable(
                                "register",
                                linkInteractionListener = { onRegisterClick() }
                            )
                        ) {
                            withStyle(
                                style = SpanStyle(
                                    color = linkColor,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Daftar")
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp)
                .size(44.dp)
                .border(
                    width = 1.dp,
                    color = toggleBorderColor,
                    shape = CircleShape
                )
                .background(
                    color = toggleBackgroundColor,
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false),
                    onClick = onToggleDarkMode
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (isDarkMode) PortalTikIcons.sun else PortalTikIcons.moon),
                contentDescription = "Toggle Theme",
                tint = toggleIconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@DevicePreviews
@Composable
private fun AuthenticationScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    PortalTikTheme {
        Scaffold { p ->
            AuthenticationScreen(
                email = email,
                password = password,
                contentPadding = PaddingValues(0.dp),
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onSignIn = {_, _ ->},
                onRegisterClick = {},
                onToggleDarkMode = {},
                modifier = Modifier.padding(p)
            )
        }
    }
}
