package kite1412.gatetik.network.backend.datasource

import kite1412.gatetik.Location
import kite1412.gatetik.domain.model.GateAccessType
import kite1412.gatetik.getPlatform
import kite1412.gatetik.model.Gate
import kite1412.gatetik.network.backend.BackendClient
import kite1412.gatetik.network.backend.dto.model.BackendGate
import kite1412.gatetik.network.backend.dto.request.BackendGateAccessRequest
import kite1412.gatetik.network.backend.dto.request.BackendGateUpdateRequest
import kite1412.gatetik.network.backend.dto.request.BackendLocationGateAccessRequest
import kite1412.gatetik.network.backend.dto.response.BackendCloseGateResponse
import kite1412.gatetik.network.backend.dto.response.BackendMainGateResponse
import kite1412.gatetik.network.backend.dto.response.BackendOpenGateResponse
import kite1412.gatetik.network.backend.dto.response.BackendResponse
import kite1412.gatetik.network.backend.extension.toUpdateRequest
import kite1412.gatetik.network.backend.util.BackendException
import kite1412.gatetik.network.domain.datasource.GateRemoteDataSource

class BackendGateDataSource : GateRemoteDataSource {
    private val accessMethod = getPlatform().type.name.lowercase()

    override suspend fun getMainGate(): Gate? = BackendClient
        .get<BackendResponse<BackendMainGateResponse>>("gate/main")
        .data
        ?.toModel()

    override suspend fun updateMainGate(gate: Gate): Gate {
        val res = BackendClient.patch<BackendGateUpdateRequest, BackendResponse<BackendGate>>(
            path = "gate",
            body = gate.toUpdateRequest()
        )

        return res.data?.toModel() ?: throw BackendException("Failed to update main gate")
    }

    override suspend fun openGate(id: Int): Boolean {
        val res = BackendClient.post<BackendGateAccessRequest, BackendResponse<BackendOpenGateResponse>>(
            path = "gate/open",
            body = gateAccess(id, true)
        )

        return res.success
    }

    override suspend fun closeGate(id: Int): Boolean {
        val res = BackendClient.post<BackendGateAccessRequest, BackendResponse<BackendCloseGateResponse>>(
            path = "gate/close",
            body = gateAccess(id, false)
        )

        return res.success
    }

    override suspend fun enterGate(
        id: Int,
        location: Location
    ): Boolean {
        val res = BackendClient.post<BackendLocationGateAccessRequest, BackendResponse<BackendOpenGateResponse>>(
            path = "gate/entry",
            body = locationGateAccess(
                id = id,
                open = true,
                location = location
            )
        )

        return res.success
    }

    override suspend fun exitGate(
        id: Int,
        location: Location
    ): Boolean {
        val res = BackendClient.post<BackendLocationGateAccessRequest, BackendResponse<BackendCloseGateResponse>>(
            path = "gate/exit",
            body = locationGateAccess(
                id = id,
                open = false,
                location = location
            )
        )

        return res.success
    }

    override suspend fun enterOrExitGate(
        id: Int,
        location: Location
    ): GateAccessType {
        val res = BackendClient.post<BackendLocationGateAccessRequest, BackendResponse<BackendOpenGateResponse>>(
            path = "gate/access",
            body = locationGateAccess(
                id = id,
                open = true,
                location = location
            )
        )

        return res.data?.let { data ->
            if (data.action == "entry") GateAccessType.ENTER
            else GateAccessType.EXIT
        } ?: throw BackendException("Failed to access gate")
    }

    private fun gateAccess(id: Int, open: Boolean) = BackendGateAccessRequest(
        gateId = id,
        accessMethod = accessMethod,
        notes = actionNotes(open)
    )

    private fun locationGateAccess(
        id: Int,
        open: Boolean,
        location: Location
    ) = BackendLocationGateAccessRequest(
        gateId = id,
        accessMethod = accessMethod,
        latitude = location.latitude,
        longitude = location.longitude,
        notes = actionNotes(open)
    )

    private fun actionNotes(isOpen: Boolean) =
        "${if (isOpen) "Open" else "Close"} request from $accessMethod platform."
}