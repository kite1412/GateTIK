package kite1412.gatetik.domain.repository

import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.model.User
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kotlinx.coroutines.flow.Flow

typealias ProfileResult<T> = Result<T, Error>

interface ProfileRepository {
    fun observeProfile(): Flow<ProfileResult<User>>

    suspend fun updateProfile(user: User): ProfileResult<User>

    suspend fun resetPassword(data: PasswordReset): ProfileResult<Boolean>

    interface ProfileError : Error {
        data class BadRequest(override val message: String) : ProfileError
    }
}