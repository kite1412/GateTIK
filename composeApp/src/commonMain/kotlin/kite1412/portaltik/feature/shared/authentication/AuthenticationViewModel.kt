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
import kite1412.portaltik.util.Result
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val dataStore: PortalTikDataStore,
    private val authentication: Authentication,
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var authResult by mutableStateOf<AuthResult<User?>>(Result.Loading)
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
        }
    }

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(!isDarkMode)
        }
    }
}
