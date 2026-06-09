package kite1412.gatetik.network.domain.datasource

import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.repository.ProfileResult
import kite1412.gatetik.model.User

interface ProfileRemoteDataSource {
    suspend fun getProfile(): ProfileResult<User>

    suspend fun updateProfile(user: User): ProfileResult<User>

    suspend fun resetPassword(data: PasswordReset): ProfileResult<Boolean>
}