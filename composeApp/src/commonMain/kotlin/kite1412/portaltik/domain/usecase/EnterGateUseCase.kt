package kite1412.portaltik.domain.usecase

import kite1412.portaltik.Location
import kite1412.portaltik.domain.repository.GateRepository

class EnterGateUseCase(private val gateRepository: GateRepository) {
    suspend operator fun invoke(id: Int, location: Location) = gateRepository.enterGate(id, location)
}