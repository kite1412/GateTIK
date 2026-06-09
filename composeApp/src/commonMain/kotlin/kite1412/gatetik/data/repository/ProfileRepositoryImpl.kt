package kite1412.gatetik.data.repository

import kite1412.gatetik.data.util.logError
import kite1412.gatetik.domain.model.PasswordReset
import kite1412.gatetik.domain.repository.ProfileRepository
import kite1412.gatetik.domain.repository.ProfileResult
import kite1412.gatetik.model.User
import kite1412.gatetik.network.domain.datasource.ProfileRemoteDataSource
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.Success
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    private val user = MutableStateFlow<ProfileResult<User>>(Result.Loading)
    private val logTag = "ProfileRepository"

    override fun observeProfile(): Flow<ProfileResult<User>> = flow {
        remoteDataSource.getProfile()
            .onSuccess {
                user.value = Success(it)
            }
            .logError(logTag)

        emitAll(user)
    }

    override suspend fun updateProfile(user: User): ProfileResult<User> = remoteDataSource
        .updateProfile(user)
        .onSuccess {
            this.user.value = Success(it)
        }
        .logError(logTag)

    override suspend fun resetPassword(data: PasswordReset): ProfileResult<Boolean> = remoteDataSource
        .resetPassword(data)
        .logError(logTag)
}