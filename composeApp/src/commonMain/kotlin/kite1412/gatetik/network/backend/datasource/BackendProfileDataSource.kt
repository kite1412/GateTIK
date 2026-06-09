package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.repository.ProfileRepository
import kite1412.gatetik.domain.repository.ProfileResult
import kite1412.gatetik.model.User
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendUser
import kite1412.gatetik.network.backend.dto.request.BackendPasswordResetRequest
import kite1412.gatetik.network.backend.dto.request.BackendProfileUpdateRequest
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toRequest
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.domain.datasource.ProfileRemoteDataSource
import kite1412.gatetik.network.domain.util.ServerError
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Success

class BackendProfileDataSource : ProfileRemoteDataSource {
    override suspend fun getProfile(): ProfileResult<User> =
        BackendClient.get<BackendResponse<BackendUser>>(
            path = "profile"
        )
            .data
            ?.toModel()
            ?.let(::Success)
            ?: Error(ServerError())

    override suspend fun updateProfile(user: User): ProfileResult<User> {
        val res = BackendClient.patch<BackendProfileUpdateRequest, BackendResponse<BackendUser>>(
            path = "profile",
            body = user.toUpdateRequest()
        )

        return if (res.errors != null) {
            val errors = res.errors

            val message = when {
                errors.contains("email") -> "Email sudah digunakan"
                errors.contains("npm_nip") -> "NPM/NIP sudah digunakan"
                errors.contains("full_name") -> "Nama terlalu panjang"
                errors.contains("phone_number") -> "Nomor telepon tidak valid"
                else -> null
            }

            Error(
                message?.let(ProfileRepository.ProfileError::BadRequest)
                    ?: ServerError()
            )
        } else if (res.data != null) Success(res.data.toModel())
        else Error(ServerError())
    }

    override suspend fun resetPassword(data: PasswordReset): ProfileResult<Boolean> {
        val res = BackendClient.patch<BackendPasswordResetRequest, BackendResponse<Unit>>(
            path = "profile/password",
            body = data.toRequest()
        )

        if (res.errors != null) {
            if (res.errors.contains("new_password"))
                return Error(ProfileRepository.ProfileError.BadRequest("Kata sandi baru harus terdiri dari minimal 8 karakter"))
        }

        return if (!res.success) Error(ProfileRepository.ProfileError.BadRequest("Kata sandi baru harus berbeda dari kata sandi saat ini"))
        else Success(true)
    }
}