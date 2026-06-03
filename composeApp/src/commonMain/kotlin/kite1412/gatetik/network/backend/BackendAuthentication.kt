package kite1412.gatetik.network.backend

import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kite1412.gatetik.File
import kite1412.gatetik.Logger
import kite1412.gatetik.common.AppCoroutineScope
import kite1412.gatetik.datastore.GateTikDataStore
import kite1412.gatetik.datastore.extension.toDataStoreUser
import kite1412.gatetik.datastore.model.DataStoreAuthSession
import kite1412.gatetik.domain.AuthResult
import kite1412.gatetik.domain.Authentication
import kite1412.gatetik.domain.SessionStatus
import kite1412.gatetik.model.User
import kite1412.gatetik.network.backend.dto.model.BackendUser
import kite1412.gatetik.network.backend.dto.request.BackendSignInRequest
import kite1412.gatetik.network.backend.dto.response.BackendLoginResponse
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.domain.util.ServerError
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class BackendAuthentication(
    appScope: AppCoroutineScope,
    private val dataStore: GateTikDataStore
) : Authentication {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val sessionStatus: Flow<SessionStatus> = dataStore.observeAuthSession()
        .mapLatest { authSession ->
            if (authSession != null) SessionStatus.SignedIn(
                token = authSession.token,
                user = authSession.user.toModel()
            ) else SessionStatus.SignedOut
        }
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = SessionStatus.Loading
        )

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult<User> = try {
        val res = BackendClient.rawPost(
            path = "auth/login",
            body = BackendSignInRequest(
                email = email,
                password = password
            ),
            useToken = false
        )
        when (res.status.value) {
            200 -> return res.body<BackendResponse<BackendLoginResponse>>().data?.let {
                val user = it.user.toModel()
                dataStore.setAuthSession(
                    DataStoreAuthSession(
                        token = it.token,
                        user = user.toDataStoreUser()
                    )
                )
                Success(user)
            } ?: Error(ServerError())
            401 -> return Error(Authentication.AuthError.InvalidCredentials())
        }
        Error(ServerError())
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to sign in",
            throwable = e
        )
        Error(ServerError())
    }

    override suspend fun signUp(
        fullName: String,
        email: String,
        npmNip: String,
        password: String,
        confirmPassword: String,
        ktm: File
    ): AuthResult<User> = try {
        if (ktm.mimeType == null) return Error(Authentication.AuthError.BadRequest("File KTM tidak valid"))
        val res = BackendClient.rawPost(
            path = "auth/register",
            useToken = false
        ) {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("full_name", fullName)
                        append("email", email)
                        append("npm_nip", npmNip)
                        append("password", password)
                        append("password_confirmation", confirmPassword)
                        append(
                            key = "ktm",
                            value = ktm.bytes,
                            headers = Headers.build {
                                append(
                                    name = HttpHeaders.ContentDisposition,
                                    value = "filename=\"${ktm.name}\""
                                )
                                append(
                                    name = HttpHeaders.ContentType,
                                    value = ktm.mimeType
                                )
                            }
                        )
                    }
                )
            )
        }.body<BackendResponse<BackendUser>>()

        if (res.errors != null) {
            val errors = res.errors
            when {
                errors.contains("email") -> return Error(Authentication.AuthError.BadRequest("Email sudah digunakan"))
                errors.contains("npm_nip") -> return Error(Authentication.AuthError.BadRequest("NPM/NIP sudah digunakan"))
                errors.contains("password") -> return Error(Authentication.AuthError.BadRequest("Password dan konfirmasi password tidak sesuai"))
            }
        }

        res.data?.toModel()?.let(::Success)
            ?: Error(Authentication.AuthError.BadRequest("Gagal registrasi, harap coba lagi"))
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to sign up",
            throwable = e
        )
        Error(ServerError())
    }

    override suspend fun logout(): AuthResult<Boolean> = try {
        dataStore.deleteAuthSession()
        Success(true)
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to sign up",
            throwable = e
        )
        Error(ServerError())
    }
}