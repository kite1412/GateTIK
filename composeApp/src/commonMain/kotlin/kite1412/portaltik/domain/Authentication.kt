package kite1412.portaltik.domain

import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Result
import kotlinx.coroutines.flow.Flow

typealias AuthResult<T> = Result<T, Error>

interface Authentication {
    val logTag: String get() = "Authentication"

    val sessionStatus: Flow<SessionStatus>

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User>

    suspend fun register(
        email: String,
        password: String,
        role: UserRole
    ): AuthResult<User>

    suspend fun logout(): AuthResult<Boolean>

    sealed interface AuthError : Error {
        data class InvalidCredentials(
            override val message: String = "Email atau password salah."
        ) : AuthError
        data class UserAlreadyExists(
            override val message: String = "Email sudah digunakan."
        ) : AuthError
        data class AccountNotValidated(
            override val message: String = "Akun belum divalidasi."
        ) : AuthError
    }
}

sealed interface SessionStatus {
    data object Loading : SessionStatus
    data object SignedOut : SessionStatus
    data class SignedIn(
        val token: String,
        val user: User
    ) : SessionStatus
}