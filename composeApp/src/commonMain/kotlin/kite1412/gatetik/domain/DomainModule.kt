package kite1412.gatetik.domain

import kite1412.gatetik.domain.usecase.AccessGateUseCase
import kite1412.gatetik.domain.usecase.EnterGateUseCase
import kite1412.gatetik.domain.usecase.ExitGateUseCase
import kite1412.gatetik.domain.usecase.GetMainCctvUseCase
import kite1412.gatetik.domain.usecase.GetMainGateUseCase
import kite1412.gatetik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.gatetik.domain.usecase.UpdateMainGateUseCase
import kite1412.gatetik.domain.usecase.UpdateMainParkingQuotaUseCase
import org.koin.dsl.module

private val useCaseModule = module {
    factory {
        GetMainGateUseCase(gateRepository = get())
    }

    factory {
        GetMainCctvUseCase(cctvRepository = get())
    }

    factory {
        GetMainParkingQuotaUseCase(parkingQuotaRepository = get())
    }

    factory {
        EnterGateUseCase(gateRepository = get())
    }

    factory {
        ExitGateUseCase(gateRepository = get())
    }

    factory {
        AccessGateUseCase(gateRepository = get())
    }

    factory {
        UpdateMainGateUseCase(gateRepository = get())
    }

    factory {
        UpdateMainParkingQuotaUseCase(parkingQuotaRepository = get())
    }
}

val domainModule = module {
    includes(useCaseModule)
}