package kite1412.portaltik.feature.shared.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.AuthResult
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.model.User
import kite1412.portaltik.ui.util.UiEvent
import kite1412.portaltik.util.Result
import kite1412.portaltik.util.onError
import kite1412.portaltik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val dataStore: PortalTikDataStore,
    private val authentication: Authentication,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var authResult by mutableStateOf<AuthResult<User>>(Result.Loading)
        private set

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authResult = Result.Loading
            authResult = authentication.signIn(email, password)
            authResult
                .onSuccess {
                    _uiEvent.emit(UiEvent.ShowSnackbar("Login berhasil"))
                }
                .onError {
                    if (it is Authentication.AuthError) {
                        _uiEvent.emit(UiEvent.ShowSnackbar(it.message))
                    }
                }
        }
    }

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(!isDarkMode)
        }
    }
}
