package kite1412.gatetik.domain.usecase

import kite1412.gatetik.Location
import kite1412.gatetik.domain.repository.GateRepository

class AccessGateUseCase(private val gateRepository: GateRepository) {
    suspend fun open(id: Int) = gateRepository.openGate(id)
    suspend fun close(id: Int) = gateRepository.closeGate(id)
    suspend fun enter(id: Int, location: Location) = gateRepository.enterGate(id, location)
    suspend fun enterOrExitGate(id: Int, location: Location) =
        gateRepository.enterOrExitGate(id, location)
}