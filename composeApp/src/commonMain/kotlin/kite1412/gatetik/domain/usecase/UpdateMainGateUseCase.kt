package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.GateRepository
import kite1412.gatetik.model.Gate

class UpdateMainGateUseCase(private val gateRepository: GateRepository) {
    suspend operator fun invoke(gate: Gate) = gateRepository.updateMainGate(gate)
}