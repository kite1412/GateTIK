package kite1412.portaltik.feature.shared.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
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
import androidx.compose.ui.unit.sp
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
import kite1412.portaltik.designsystem.theme.White30
import kite1412.portaltik.designsystem.theme.White50
import kite1412.portaltik.designsystem.theme.White60
import kite1412.portaltik.designsystem.util.PortalTikIcons
import kite1412.portaltik.ui.component.SmallCircularProgressIndicator
import kite1412.portaltik.ui.compositionlocal.LocalDarkMode
import kite1412.portaltik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.portaltik.ui.preview.DevicePreviews
import kite1412.portaltik.ui.util.UiEvent
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: AuthenticationViewModel = koinViewModel()
) {
    val isDarkMode = LocalDarkMode.current
    val snackbarHostStateWrapper = LocalSnackbarHostStateWrapper.current
    val email = viewModel.email
    val fullName = viewModel.fullName
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar) snackbarHostStateWrapper
                .showSnackbar(event.message)
        }
    }
    AuthenticationScreen(
        email = email,
        fullName = fullName,
        password = password,
        confirmPassword = confirmPassword,
        isInProgress = viewModel.isInProgress,
        contentPadding = contentPadding,
        onEmailChange = viewModel::onEmailChange,
        onFullNameChange = viewModel::onFullNameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSignIn = viewModel::signIn,
        onSignUp = { _, _, _, _ -> },
        onToggleDarkMode = { viewModel.toggleDarkMode(isDarkMode) },
        modifier = modifier,
    )
}

@Composable
private fun AuthenticationScreen(
    email: String,
    fullName: String,
    password: String,
    confirmPassword: String,
    isInProgress: Boolean,
    contentPadding: PaddingValues,
    onEmailChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (fullName: String, email: String, password: String, confirmPassword: String) -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    val gradientColor1 by animateColorAsState(if (isDarkMode) BlueSlateGradient[0] else LighterBlueLightBlueGradient[0])
    val gradientColor2 by animateColorAsState(if (isDarkMode) BlueSlateGradient[1] else LighterBlueLightBlueGradient[1])
    val backgroundBrush = Brush.verticalGradient(listOf(gradientColor1, gradientColor2))
    var isSignIn by retain { mutableStateOf(true) }

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

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(
                targetState = isSignIn,
                modifier = Modifier.padding(start = 8.dp),
                transitionSpec = {
                    fadeIn() + slideInHorizontally { -it } togetherWith fadeOut() + slideOutHorizontally { -it }
                }
            ) {
                Text(
                    text = if (it) "Login" else "Registrasi",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 32.sp
                )
            }
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
                val secondaryTextColor by animateColorAsState(if (isDarkMode) White50 else MaterialTheme.colorScheme.onSurfaceVariant)
                val linkColor by animateColorAsState(if (!isDarkMode) Blue500 else Blue300)

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
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
                    }
                    item {
                        Form(
                            isSignIn = isSignIn,
                            fullName = fullName,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            onFullNameChange = onFullNameChange,
                            onEmailChange = onEmailChange,
                            onPasswordChange = onPasswordChange,
                            onConfirmPasswordChange = onConfirmPasswordChange,
                            isDarkMode = isDarkMode
                        )
                    }
                    item {
                        GradientTextButton(
                            text = if (!isInProgress) "Masuk" else "",
                            onClick = {
                                if (isSignIn) onSignIn(email, password)
                                else onSignUp(fullName, email, password, confirmPassword)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = email.isNotBlank() && password.isNotBlank()
                                    && (if (!isSignIn) fullName.isNotBlank() && password.length >= 8 && confirmPassword == password else true)
                                    && !isInProgress,
                            leading = if (isInProgress) {
                                {
                                    SmallCircularProgressIndicator(color = White30)
                                }
                            } else null
                        )
                    }
                    item {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = secondaryTextColor
                                    )
                                ) {
                                    append(if (isSignIn) "Belum punya akun? " else "Sudah punya akun? ")
                                }
                                withLink(
                                    link = LinkAnnotation.Clickable(
                                        "state-change",
                                        linkInteractionListener = { isSignIn = !isSignIn }
                                    )
                                ) {
                                    withStyle(
                                        style = SpanStyle(
                                            color = linkColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(if (isSignIn) "Daftar" else "Masuk")
                                    }
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
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

@Composable
private fun Form(
    isSignIn: Boolean,
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(if (isDarkMode) White50 else RoyalBlue800_50)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var showPassword by retain { mutableStateOf(false) }
        var showConfirmPassword by retain { mutableStateOf(false) }

        AnimatedVisibility(!isSignIn) {
            FormOutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = buildAnnotatedString { append("NAMA LENGKAP") },
                placeholder = "Masukkan nama lengkap",
                icon = PortalTikIcons.person,
                iconColor = iconColor,
                isDarkMode = isDarkMode
            )
        }
        FormOutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = buildAnnotatedString { append("EMAIL KAMPUS") },
            placeholder = "@students.unila.ac.id",
            icon = PortalTikIcons.email,
            iconColor = iconColor,
            isDarkMode = isDarkMode,
            keyboardType = KeyboardType.Email
        )
        FormOutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = buildAnnotatedString { append("PASSWORD") },
            placeholder = "Password",
            icon = PortalTikIcons.lock,
            iconColor = iconColor,
            isDarkMode = isDarkMode,
            visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            actions = {
                PasswordVisibilityStateIcon(
                    showPassword = showPassword,
                    iconColor = iconColor,
                    onShowPasswordChange = { showPassword = !showPassword }
                )
            }
        )
        AnimatedVisibility(!isSignIn) {
            FormOutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = buildAnnotatedString { append("KONFIRMASI PASSWORD") },
                placeholder = "Ulangi password",
                icon = PortalTikIcons.lock,
                iconColor = iconColor,
                isDarkMode = isDarkMode,
                visualTransformation = if (showConfirmPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                actions = {
                    PasswordVisibilityStateIcon(
                        showPassword = showConfirmPassword,
                        iconColor = iconColor,
                        onShowPasswordChange = { showConfirmPassword = !showConfirmPassword }
                    )
                }
            )
        }
    }
}

@Composable
private fun PasswordVisibilityStateIcon(
    showPassword: Boolean,
    iconColor: Color,
    onShowPasswordChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            resource = if (showPassword) PortalTikIcons.eyeClose else PortalTikIcons.eyeOpen
        ),
        contentDescription = if (showPassword) "sembunyikan password" else "tampilkan password",
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null
        ) { onShowPasswordChange(!showPassword) },
        tint = iconColor
    )
}

@Composable
private fun FormOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: AnnotatedString,
    placeholder: String,
    icon: DrawableResource,
    iconColor: Color,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    actions: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label,
        placeholder = placeholder,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardType = keyboardType,
        leading = {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Lock Icon",
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        },
        actions = actions
    )
}

@DevicePreviews
@Composable
private fun AuthenticationScreenPreview() {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    PortalTikTheme {
        Scaffold { p ->
            AuthenticationScreen(
                email = email,
                fullName = fullName,
                password = password,
                confirmPassword = confirmPassword,
                isInProgress = true,
                contentPadding = PaddingValues(0.dp),
                onEmailChange = { email = it },
                onFullNameChange = { fullName = it },
                onPasswordChange = { password = it },
                onConfirmPasswordChange = { confirmPassword = it },
                onSignIn = {_, _ ->},
                onSignUp = {_, _, _, _ ->},
                onToggleDarkMode = {},
                modifier = Modifier.padding(p)
            )
        }
    }
}
