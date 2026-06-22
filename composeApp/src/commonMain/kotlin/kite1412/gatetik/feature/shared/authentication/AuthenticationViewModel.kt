package kite1412.gatetik.feature.shared.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.File
import kite1412.gatetik.PickResult
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.AuthResult
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.model.User
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.showSnackbar
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val dataStore: GateTikDataStore,
    private val authentication: Authentication
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    var isSignIn by mutableStateOf(true)
        private set
    var fullName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var npmNip by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var idCard by mutableStateOf<File?>(null)
        private set
    var authResult by mutableStateOf<AuthResult<User>>(Result.Loading)
        private set
    var isInProgress by mutableStateOf(false)
        private set

    fun onIsSignInChange(value: Boolean) {
        this.isSignIn = value
    }

    fun onFullNameChange(name: String) {
        this.fullName = name
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onNpmNipChange(value: String) {
        this.npmNip = value
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun onConfirmPasswordChange(password: String) {
        this.confirmPassword = password
    }

    fun onIdCardPick(result: PickResult) {
        viewModelScope.launch {
            when (result) {
                is PickResult.Success -> this@AuthenticationViewModel.idCard = result.file
                is PickResult.Failed -> result.reason.message?.let { message ->
                    _uiEvent.showSnackbar(message)
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            isInProgress = true
            authResult = Result.Loading
            authResult = authentication.signIn(email, password)
                .onSuccess {
                    _uiEvent.showSnackbar("Login berhasil")
                }
                .onError {
                    _uiEvent.showSnackbar(it.message)
                }
            isInProgress = false
        }
    }

    fun signUp(
        fullName: String,
        email: String,
        npmNip: String,
        password: String,
        confirmPassword: String,
        idCard: File
    ) {
        viewModelScope.launch {
            isInProgress = true
            authResult = Result.Loading
            authResult = authentication.signUp(
                fullName = fullName,
                email = email,
                npmNip = npmNip,
                password = password,
                confirmPassword = confirmPassword,
                ktm = idCard
            )
                .onSuccess {
                    _uiEvent.showSnackbar("Registrasi berhasil, harap tunggu validasi admin")
                    with(this@AuthenticationViewModel) {
                        this.isSignIn = true
                        this.fullName = ""
                        this.email = ""
                        this.npmNip = ""
                        this.password = ""
                        this.confirmPassword = ""
                        this.idCard = null
                    }
                }
                .onError {
                    _uiEvent.showSnackbar(it.message)
                }
            isInProgress = false
        }
    }

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(!isDarkMode)
        }
    }
}
