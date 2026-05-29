package kite1412.portaltik.network.mock

import kite1412.portaltik.domain.AuthResult
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.domain.SessionStatus
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.util.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthentication : Authentication {
    private val _sessionStatus = MutableStateFlow<SessionStatus>(
        SessionStatus.SignedIn(
            token = "",
            user = mockUser
        )
    )
    override val sessionStatus: Flow<SessionStatus> = _sessionStatus.asStateFlow()

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User> {
        _sessionStatus.value = SessionStatus.SignedIn(
            token = "",
            user = mockUser
        )
        return Success(mockUser)
    }

    override suspend fun register(
        email: String,
        password: String,
        role: UserRole
    ): AuthResult<User> = Success(mockUser)

    override suspend fun logout(): AuthResult<Boolean> {
        _sessionStatus.value = SessionStatus.SignedOut

        return Success(true)
    }
}