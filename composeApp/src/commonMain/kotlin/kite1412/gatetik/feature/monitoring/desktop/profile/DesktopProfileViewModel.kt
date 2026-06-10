package kite1412.gatetik.feature.monitoring.desktop.profile

import androidx.lifecycle.viewModelScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.usecase.ResetPasswordUseCase
import kite1412.gatetik.domain.usecase.UpdateProfileUseCase
import kite1412.gatetik.feature.monitoring.desktop.DesktopBaseViewModel
import kite1412.gatetik.model.User
import kite1412.gatetik.ui.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class DesktopProfileViewModel(
    dataStore: GateTikDataStore,
    private val authentication: Authentication,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : DesktopBaseViewModel(authentication, dataStore) {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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