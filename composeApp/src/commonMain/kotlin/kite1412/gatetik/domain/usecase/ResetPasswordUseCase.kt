package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.repository.ProfileRepository
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow

class ResetPasswordUseCase(private val profileRepository: ProfileRepository) {
    suspend fun resetAndNotify(data: PasswordReset, uiEventFlow: MutableSharedFlow<UiEvent>) =
        profileRepository.resetPassword(data)
            .onSuccess {
                uiEventFlow.emit(
                    UiEvent.ShowSnackbar("Berhasil mengubah password")
                )
            }
            .onError {
                uiEventFlow.emit(
                    UiEvent.ShowSnackbar("Gagal mengubah password, harap coba lagi")
                )
            }
}