package kite1412.portaltik.domain.usecase

import kite1412.portaltik.Location
import kite1412.portaltik.domain.repository.GateRepository

class OpenGateUseCase(private val gateRepository: GateRepository) {
    suspend operator fun invoke(id: Int) = gateRepository.openGate(id)
    suspend fun enter(id: Int, location: Location) = gateRepository.enterGate(id, location)
}