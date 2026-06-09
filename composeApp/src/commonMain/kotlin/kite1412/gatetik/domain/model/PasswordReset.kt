package kite1412.gatetik.domain.model

data class PasswordReset(
    val currentPassword: String,
    val newPassword: String,
    val newPasswordConfirmation: String
)
