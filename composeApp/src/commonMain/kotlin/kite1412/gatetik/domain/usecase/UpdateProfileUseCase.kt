package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.ProfileRepository
import kite1412.gatetik.model.User
import kite1412.gatetik.ui.util.UiEvent
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow

class UpdateProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend fun updateAndNotify(user: User, uiEventFlow: MutableSharedFlow<UiEvent>) =
        profileRepository.updateProfile(user)
            .onSuccess {
                uiEventFlow.emit(
                    UiEvent.ShowSnackbar("Berhasil memperbarui profil")
                )
            }
            .onError {
                uiEventFlow.emit(
                    UiEvent.ShowSnackbar("Gagal memperbarui profil, harap coba lagi")
                )
            }
}