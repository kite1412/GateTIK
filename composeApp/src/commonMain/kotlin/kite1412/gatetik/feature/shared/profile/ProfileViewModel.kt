package kite1412.gatetik.feature.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.usecase.ResetPasswordUseCase
import kite1412.gatetik.domain.usecase.UpdateProfileUseCase
import kite1412.gatetik.model.User
import kite1412.gatetik.ui.util.LoadState
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.ui.util.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authentication: Authentication,
    private val dataStore: GateTikDataStore,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val user: Flow<LoadState<User>> = flow {
        emit(LoadState.Loading("Memuat informasi akun"))

        val sessionStatus = authentication.sessionStatus.first()
        if (sessionStatus is SessionStatus.SignedIn)
            emit(LoadState.Success(sessionStatus.user))
    }
        .stateIn(viewModelScope)

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(darkMode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authentication.logout()
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            updateProfileUseCase.updateAndNotify(user, _uiEvent)
        }
    }

    fun resetPassword(data: PasswordReset) {
        viewModelScope.launch {
            resetPasswordUseCase.resetAndNotify(data, _uiEvent)
        }
    }
}