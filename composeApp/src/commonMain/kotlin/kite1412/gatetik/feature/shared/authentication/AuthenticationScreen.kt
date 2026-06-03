package kite1412.gatetik.feature.shared.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kite1412.gatetik.File
import kite1412.gatetik.PickResult
import kite1412.gatetik.designsystem.component.GlassBox
import kite1412.gatetik.designsystem.component.GradientTextButton
import kite1412.gatetik.designsystem.component.Icon
import kite1412.gatetik.designsystem.component.OutlinedTextField
import kite1412.gatetik.designsystem.extension.radialBackground
import kite1412.gatetik.designsystem.theme.Blue300
import kite1412.gatetik.designsystem.theme.Blue500
import kite1412.gatetik.designsystem.theme.BlueSlateGradient
import kite1412.gatetik.designsystem.theme.GateTikTheme
import kite1412.gatetik.designsystem.theme.LighterBlueLightBlueGradient
import kite1412.gatetik.designsystem.theme.RoyalBlue800_50
import kite1412.gatetik.designsystem.theme.RoyalBlue800_60
import kite1412.gatetik.designsystem.theme.White
import kite1412.gatetik.designsystem.theme.White30
import kite1412.gatetik.designsystem.theme.White50
import kite1412.gatetik.designsystem.theme.White60
import kite1412.gatetik.designsystem.util.GateTikIcons
import kite1412.gatetik.rememberFilePicker
import kite1412.gatetik.ui.component.SmallCircularProgressIndicator
import kite1412.gatetik.ui.component.ThemeToggle
import kite1412.gatetik.ui.compositionlocal.LocalDarkMode
import kite1412.gatetik.ui.compositionlocal.LocalSnackbarHostStateWrapper
import kite1412.gatetik.ui.preview.DevicePreviews
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.util.mb
import kotlinx.coroutines.launch
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
    val fullName = viewModel.fullName
    val email = viewModel.email
    val npmNip = viewModel.npmNip
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword
    val idCard = viewModel.idCard

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar) snackbarHostStateWrapper
                .showSnackbar(event.message)
        }
    }
    AuthenticationScreen(
        isSignIn = viewModel.isSignIn,
        fullName = fullName,
        email = email,
        npmNip = npmNip,
        password = password,
        confirmPassword = confirmPassword,
        idCard = idCard,
        isInProgress = viewModel.isInProgress,
        contentPadding = contentPadding,
        onIsSignInChange = viewModel::onIsSignInChange,
        onFullNameChange = viewModel::onFullNameChange,
        onEmailChange = viewModel::onEmailChange,
        onNpmNipChange = viewModel::onNpmNipChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onIdCardPick = viewModel::onIdCardPick,
        onSignIn = viewModel::signIn,
        onSignUp = viewModel::signUp,
        onToggleDarkMode = { viewModel.toggleDarkMode(isDarkMode) },
        modifier = modifier,
    )
}

@Composable
private fun AuthenticationScreen(
    isSignIn: Boolean,
    fullName: String,
    email: String,
    npmNip: String,
    password: String,
    confirmPassword: String,
    idCard: File?,
    isInProgress: Boolean,
    contentPadding: PaddingValues,
    onIsSignInChange: (Boolean) -> Unit,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNpmNipChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onIdCardPick: (PickResult) -> Unit,
    onSignIn: (email: String, password: String) -> Unit,
    onSignUp: (
        fullName: String,
        email: String,
        npmNip: String,
        password: String,
        confirmPassword: String,
        idCard: File
    ) -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = LocalDarkMode.current
    val gradientColor1 by animateColorAsState(if (isDarkMode) BlueSlateGradient[0] else LighterBlueLightBlueGradient[0])
    val gradientColor2 by animateColorAsState(if (isDarkMode) BlueSlateGradient[1] else LighterBlueLightBlueGradient[1])
    val backgroundBrush = Brush.verticalGradient(listOf(gradientColor1, gradientColor2))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .run {
                if (isDarkMode) radialBackground()
                else this
            }
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
            ThemeToggle(onToggle = onToggleDarkMode)
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
            val lazyListState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = lazyListState
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(GateTikIcons.unila),
                            contentDescription = "App Icon",
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Gate TIK",
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
                        npmNip = npmNip,
                        password = password,
                        confirmPassword = confirmPassword,
                        idCard = idCard,
                        onFullNameChange = onFullNameChange,
                        onEmailChange = onEmailChange,
                        onNpmNipChange = onNpmNipChange,
                        onPasswordChange = onPasswordChange,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        onIdCardPick = onIdCardPick,
                        isDarkMode = isDarkMode
                    )
                }
                item {
                    GradientTextButton(
                        text = if (!isInProgress) "Masuk" else "",
                        onClick = {
                            if (isSignIn) onSignIn(email, password)
                            else idCard?.let { idCard ->
                                onSignUp(
                                    fullName,
                                    email,
                                    npmNip,
                                    password,
                                    confirmPassword,
                                    idCard
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotBlank() && password.isNotBlank()
                                && (if (!isSignIn) fullName.isNotBlank() && password.length >= 8 && confirmPassword == password && idCard != null else true)
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
                                    linkInteractionListener = { onIsSignInChange(!isSignIn) }
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
            this@Column.AnimatedVisibility(
                visible = lazyListState.canScrollForward,
                modifier = Modifier.align(Alignment.BottomEnd),
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val yOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 16f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(300),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                GlassBox(
                    modifier = Modifier.offset {
                        val px = yOffset.toDp().roundToPx()
                        IntOffset(x = 0, y = px)
                    },
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Icon(
                        painter = painterResource(GateTikIcons.chevronRight),
                        contentDescription = null,
                        modifier = Modifier.rotate(90f)
                    )
                }
            }
        }
    }
}

@Composable
private fun Form(
    isSignIn: Boolean,
    fullName: String,
    email: String,
    npmNip: String,
    password: String,
    confirmPassword: String,
    idCard: File?,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onNpmNipChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onIdCardPick: (PickResult) -> Unit,
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
                icon = GateTikIcons.person,
                iconColor = iconColor
            )
        }
        FormOutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = buildAnnotatedString { append("EMAIL KAMPUS") },
            placeholder = "@students.unila.ac.id",
            icon = GateTikIcons.email,
            iconColor = iconColor,
            keyboardType = KeyboardType.Email
        )
        AnimatedVisibility(!isSignIn) {
            FormOutlinedTextField(
                value = npmNip,
                onValueChange = onNpmNipChange,
                label = buildAnnotatedString { append("NPM/NIP") },
                placeholder = "NPM/NIP",
                icon = GateTikIcons.numbers,
                iconColor = iconColor
            )
        }
        FormOutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = buildAnnotatedString { append("PASSWORD") },
            placeholder = "Password",
            icon = GateTikIcons.lock,
            iconColor = iconColor,
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
                icon = GateTikIcons.lock,
                iconColor = iconColor,
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
        AnimatedVisibility(!isSignIn) {
            IdCardPicker(
                label = "KTM",
                pickedFile = idCard,
                isDarkMode = isDarkMode,
                onFilePick = onIdCardPick
            )
        }
    }
}

@Composable
private fun IdCardPicker(
    label: String,
    pickedFile: File?,
    isDarkMode: Boolean,
    onFilePick: (PickResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val filePicker = rememberFilePicker(
        acceptedMimeTypes = arrayOf("image/png", "image/jpeg"),
        maxFileSize = 2.mb
    )
    val scope = rememberCoroutineScope()
    val labelColor by animateColorAsState(if (!isDarkMode) RoyalBlue800_50 else White50)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = labelColor
        )
        GlassBox(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = null
                ) {
                    scope.launch {
                        onFilePick(filePicker.pickFile())
                    }
                },
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                val labelMedium = MaterialTheme.typography.labelMedium
                val color = MaterialTheme.colorScheme.onBackground

                Icon(
                    painter = painterResource(GateTikIcons.idCard),
                    contentDescription = null,
                    modifier = Modifier.size((labelMedium.fontSize.value * 2).dp),
                    tint = color
                )
                Text(
                    text = pickedFile?.name ?: label,
                    style = labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = color
                )
            }
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
            resource = if (showPassword) GateTikIcons.eyeClose else GateTikIcons.eyeOpen
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
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var npmNip by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    GateTikTheme {
        Scaffold { p ->
            AuthenticationScreen(
                isSignIn = true,
                fullName = fullName,
                email = email,
                npmNip = npmNip,
                password = password,
                confirmPassword = confirmPassword,
                idCard = null,
                isInProgress = true,
                contentPadding = PaddingValues(0.dp),
                onIsSignInChange = {},
                onFullNameChange = { fullName = it },
                onEmailChange = { email = it },
                onNpmNipChange = { npmNip = it },
                onPasswordChange = { password = it },
                onConfirmPasswordChange = { confirmPassword = it },
                onIdCardPick = {},
                onSignIn = {_, _ ->},
                onSignUp = {_, _, _, _, _, _ ->},
                onToggleDarkMode = {},
                modifier = Modifier.padding(p)
            )
        }
    }
}
