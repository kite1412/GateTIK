package kite1412.gatetik.domain

import kite1412.gatetik.File
import kite1412.gatetik.model.User
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kotlinx.coroutines.flow.Flow

typealias AuthResult<T> = Result<T, Error>

interface Authentication {
    val logTag: String get() = "Authentication"

    val sessionStatus: Flow<SessionStatus>

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User>

    suspend fun signUp(
        fullName: String,
        email: String,
        npmNip: String,
        password: String,
        confirmPassword: String,
        ktm: File
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
        data class BadRequest(
            override val message: String
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