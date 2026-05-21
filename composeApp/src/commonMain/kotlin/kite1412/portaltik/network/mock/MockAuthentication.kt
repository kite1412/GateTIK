package kite1412.portaltik.network.mock

import kite1412.portaltik.domain.AuthResult
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.model.UserStatus
import kite1412.portaltik.util.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthentication : Authentication {
    private val _loggedInUser = MutableStateFlow<User?>(null)
    override val signedInUser: Flow<User?> = _loggedInUser.asStateFlow()

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User> = Success(
        data = User(
            id = 0,
            fullName = "Mock User",
            email = "mock@portaltik.com",
            role = UserRole.ADMIN,
            status = UserStatus.ACTIVE,
        )
            .also {
                _loggedInUser.value = it
            }
    )

    override suspend fun register(
        email: String,
        password: String,
        role: UserRole
    ): AuthResult<User> = Success(
        data = User(
            id = 0,
            fullName = "Mock User",
            email = "mock@portaltik.com",
            role = UserRole.ADMIN,
            status = UserStatus.ACTIVE
        )
    )

    override suspend fun logout(): AuthResult<Boolean> {
        _loggedInUser.value = null

        return Success(true)
    }
}