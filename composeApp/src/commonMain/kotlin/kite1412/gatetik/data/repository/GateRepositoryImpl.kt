package kite1412.gatetik.data.repository

import kite1412.gatetik.Location
import kite1412.gatetik.data.util.tryOrThrowUnknown
import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.domain.repository.GateResult
import kite1412.gatetik.model.Gate
import kite1412.gatetik.network.domain.datasource.GateRemoteDataSource

class GateRepositoryImpl(
    private val remoteDataSource: GateRemoteDataSource,
    private val parkingQuotaRepositoryImpl: ParkingQuotaRepositoryImpl
) : GateRepository {
    private val logTag = "GateRepositoryImpl"

    override suspend fun getMainGate(): GateResult<Gate?> = tryOrThrowUnknown(
        logTag = logTag,
        errorMessage = "Failed to get main gate",
        action = remoteDataSource::getMainGate
    )

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
        if (isSuccess) parkingQuotaRepositoryImpl.updateParkingQuotaFlow { parkingQuota ->
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
        if (isSuccess) parkingQuotaRepositoryImpl.updateParkingQuotaFlow { parkingQuota ->
            parkingQuota?.let {
                parkingQuota.copy(
                    usedSlots = parkingQuota.usedSlots - 1
                )
            }
        }
        isSuccess
    }
}