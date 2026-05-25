package kite1412.portaltik.network.backend.datasource

import kite1412.portaltik.model.Gate
import kite1412.portaltik.network.backend.BackendClient
import kite1412.portaltik.network.backend.dto.model.BackendResponse
import kite1412.portaltik.network.backend.dto.request.BackendRequestOpenGate
import kite1412.portaltik.network.backend.response.BackendOpenGate
import kite1412.portaltik.network.domain.datasource.GateRemoteDataSource
import kite1412.portaltik.network.mock.mockGate

class BackendGateDataSource : GateRemoteDataSource {
    override suspend fun getGates(): List<Gate> = listOf(mockGate)

    override suspend fun getMainGate(): Gate = mockGate

    override suspend fun openGate(id: Int): Boolean {
        val res = BackendClient.post<BackendRequestOpenGate, BackendResponse<BackendOpenGate>>(
            path = "gate/open",
            body = BackendRequestOpenGate(
                gateId = id,
                accessMethod = "mobile",
                notes = "Request open from mobile device."
            )
        )

        return res.success
    }
}