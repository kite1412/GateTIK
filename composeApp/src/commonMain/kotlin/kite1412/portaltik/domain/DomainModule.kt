package kite1412.portaltik.domain

import kite1412.portaltik.domain.usecase.GetMainCctvUseCase
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetIotDeviceUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import org.koin.dsl.module

private val useCaseModule = module {
    factory {
        GetMainGateUseCase(gateRepository = get())
    }

    factory {
        GetIotDeviceUseCase(iotDeviceRepository = get())
    }

    factory {
        GetMainCctvUseCase(cctvRepository = get())
    }

    factory {
        GetMainParkingQuotaUseCase(parkingQuotaRepository = get())
    }
}

val domainModule = module {
    includes(useCaseModule)
}