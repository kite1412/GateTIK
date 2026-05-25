package kite1412.portaltik.domain.usecase

import kite1412.portaltik.domain.repository.GateRepository

class CloseGateUseCase(private val gateRepository: GateRepository) {
    suspend operator fun invoke(id: Int) = gateRepository.closeGate(id)
}