package kite1412.gatetik.network.backend.extension

import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.network.backend.dto.request.BackendPasswordResetRequest

fun PasswordReset.toRequest() = BackendPasswordResetRequest(
    currentPassword = currentPassword,
    newPassword = newPassword,
    newPasswordConfirmation = newPasswordConfirmation
)