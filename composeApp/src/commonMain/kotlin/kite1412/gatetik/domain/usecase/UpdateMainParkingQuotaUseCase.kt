package kite1412.gatetik.domain.usecase

import kite1412.gatetik.domain.repository.ParkingQuotaRepository
import kite1412.gatetik.model.ParkingQuota

class UpdateMainParkingQuotaUseCase(private val parkingQuotaRepository: ParkingQuotaRepository) {
    suspend operator fun invoke(parkingQuota: ParkingQuota) =
        parkingQuotaRepository.updateMainParkingQuota(parkingQuota)
}