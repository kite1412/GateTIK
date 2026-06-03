package kite1412.gatetik.domain.usecase

import kite1412.gatetik.Location
import kite1412.gatetik.domain.repository.GateRepository

class OpenGateUseCase(private val gateRepository: GateRepository) {
    suspend operator fun invoke(id: Int) = gateRepository.openGate(id)
    suspend fun enter(id: Int, location: Location) = gateRepository.enterGate(id, location)
}