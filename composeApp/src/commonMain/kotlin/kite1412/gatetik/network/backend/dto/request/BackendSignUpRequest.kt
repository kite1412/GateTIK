package kite1412.gatetik.network.backend.dto.request

data class BackendSignUpRequest(
    val fullName: String,
    val email: String,
    val npmNip: String,
    val password: String,
    val passwordConfirmation: String,
    val ktm: BackendFile
)
