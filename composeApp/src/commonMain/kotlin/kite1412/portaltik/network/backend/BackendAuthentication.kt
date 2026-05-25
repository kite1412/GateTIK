package kite1412.portaltik.network.backend

import io.ktor.client.call.body
import kite1412.portaltik.Logger
import kite1412.portaltik.common.AppCoroutineScope
import kite1412.portaltik.datastore.PortalTikDataStore
import kite1412.portaltik.domain.AuthResult
import kite1412.portaltik.domain.Authentication
import kite1412.portaltik.model.User
import kite1412.portaltik.model.UserRole
import kite1412.portaltik.network.backend.dto.model.BackendResponse
import kite1412.portaltik.network.backend.dto.request.BackendLogin
import kite1412.portaltik.network.backend.response.BackendLoginResponse
import kite1412.portaltik.util.Error
import kite1412.portaltik.util.Success
import kite1412.portaltik.util.Unknown
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BackendAuthentication(
    appScope: AppCoroutineScope,
    private val dataStore: PortalTikDataStore
) : Authentication {
    private val _signedInUser = MutableStateFlow<User?>(null)
    override val signedInUser: Flow<User?> = _signedInUser.asStateFlow()

    init {
        appScope.launch {
            dataStore.observeUser()
                .onEach { user ->
                    _signedInUser.value = user
                }
                .launchIn(this)
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User?> = try {
        val res = BackendClient.rawPost(
            path = "auth/login",
            body = BackendLogin(
                email = email,
                password = password
            ),
            useToken = false
        )
        when (res.status.value) {
            200 -> res.body<BackendResponse<BackendLoginResponse>>().data?.let {
                val user = it.user.toModel()
                _signedInUser.value = user
                dataStore.setToken(it.token)
                dataStore.setUser(user)
                Success(user)
            } ?: Error(Unknown())
            401 -> Success(null)
        }
        Success(null)
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to sign in",
            throwable = e
        )
        Error(Unknown())
    }

    override suspend fun register(
        email: String,
        password: String,
        role: UserRole
    ): AuthResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): AuthResult<Boolean> = try {
        dataStore.deleteUser()
        Success(true)
    } catch (e: Exception) {
        Error(Unknown())
    }
}