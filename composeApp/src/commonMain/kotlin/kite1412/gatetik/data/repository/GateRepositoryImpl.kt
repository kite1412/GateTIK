package kite1412.gatetik.data.repository

import kite1412.gatetik.Location
import kite1412.gatetik.Logger
import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.model.GateAccessType
import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.domain.repository.GateResult
import kite1412.gatetik.model.Gate
import kite1412.gatetik.network.domain.datasource.GateRemoteDataSource
import kite1412.gatetik.util.Error
import kite1412.gatetik.util.Result
import kite1412.gatetik.util.Success
import kite1412.gatetik.util.Unknown
import kite1412.gatetik.util.onError
import kite1412.gatetik.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class GateRepositoryImpl(
    private val remoteDataSource: GateRemoteDataSource,
    private val parkingQuotaRepositoryImpl: ParkingQuotaRepositoryImpl
) : GateRepository {
    private val mainGate = MutableStateFlow<GateResult<Gate?>>(Result.Loading)
    private val logTag = "GateRepositoryImpl"

    override fun observeMainGate(): Flow<GateResult<Gate?>> = flow {
        getMainGate()
            .onSuccess {
                mainGate.value = Success(it)
            }
            .onError {
                mainGate.value = Error(Unknown())
            }

        emitAll(mainGate)
    }

    override suspend fun getMainGate(): GateResult<Gate?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main gate",
        action = remoteDataSource::getMainGate
    )

    override suspend fun updateMainGate(gate: Gate): GateResult<Gate> = try {
        if (gate.allowedRadiusMeter < 0)
            return Error(GateRepository.GateError.BadRequest("Radius tidak boleh kurang dari 0"))

        val res = remoteDataSource.updateMainGate(gate)

        mainGate.value = Success(res)
        Success(res)
    } catch (e: Exception) {
        Logger.e(
            tag = logTag,
            message = "Failed to update main gate",
            throwable = e
        )
        Error(Unknown())
    }

    override suspend fun openGate(id: Int): GateResult<Boolean> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to open gate"
    ) { _ -> remoteDataSource.openGate(id) }

    override suspend fun closeGate(id: Int): GateResult<Boolean> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to close gate"
    ) { _ -> remoteDataSource.closeGate(id) }

    override suspend fun enterGate(
        id: Int,
        location: Location
    ): GateResult<Boolean> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to enter gate"
    ) { _ ->
        val isSuccess = remoteDataSource.enterGate(id, location)
        if (isSuccess) parkingQuotaRepositoryImpl.updateSuccessParkingQuotaFlow { parkingQuota ->
            parkingQuota?.let {
                parkingQuota.copy(
                    usedSlots = parkingQuota.usedSlots + 1
                )
            }
        }
        isSuccess
    }

    override suspend fun exitGate(
        id: Int,
        location: Location
    ): GateResult<Boolean> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to exit gate"
    ) { _ ->
        val isSuccess = remoteDataSource.exitGate(id, location)
        if (isSuccess) parkingQuotaRepositoryImpl.updateSuccessParkingQuotaFlow { parkingQuota ->
            parkingQuota?.let {
                parkingQuota.copy(
                    usedSlots = parkingQuota.usedSlots - 1
                )
            }
        }
        isSuccess
    }

    override suspend fun enterOrExitGate(
        id: Int,
        location: Location
    ): GateResult<GateAccessType> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to access gate"
    ) { throwError ->
        val res = remoteDataSource.enterOrExitGate(id, location)

        res?.also { type ->
            parkingQuotaRepositoryImpl.updateSuccessParkingQuotaFlow { parkingQuota ->
                parkingQuota?.let {
                    parkingQuota.copy(
                        usedSlots = when (type) {
                            GateAccessType.ENTER -> parkingQuota.usedSlots + 1
                            GateAccessType.EXIT -> parkingQuota.usedSlots - 1
                        }
                    )
                }
            }
        } ?: throwError()
    }
}