package kite1412.portaltik.network.backend.datasource

import kite1412.portaltik.getDeviceType
import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.backend.BackendClient
import kite1412.portaltik.network.backend.dto.model.BackendGate
import kite1412.portaltik.network.backend.dto.request.BackendRequestGateAction
import kite1412.portaltik.network.backend.dto.response.BackendCloseGateResponse
import kite1412.portaltik.network.backend.dto.response.BackendOpenGateResponse
import kite1412.portaltik.network.backend.dto.response.BackendResponse
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource

class BackendGateDataSource : GateRemoteDataSource {
    private val deviceType = getDeviceType()

    override suspend fun getMainGate(): Gate? = BackendClient
        .get<BackendResponse<BackendGate>>("gate/main")
        .data
        ?.toModel()

    override suspend fun openGate(id: Int): Boolean {
        val res = BackendClient.post<BackendRequestGateAction, BackendResponse<BackendOpenGateResponse>>(
            path = "gate/open",
            body = gateAction(id, true)
        )

        return res.success
    }

    override suspend fun closeGate(id: Int): Boolean {
        val res = BackendClient.post<BackendRequestGateAction, BackendResponse<BackendCloseGateResponse>>(
            path = "gate/close",
            body = gateAction(id, false)
        )

        return res.success
    }

    private fun gateAction(id: Int, open: Boolean) = BackendRequestGateAction(
        gateId = id,
        accessMethod = deviceType.name.lowercase(),
        notes = "${if (open) "Open" else "Close"} request from ${deviceType.name}."
    )
}