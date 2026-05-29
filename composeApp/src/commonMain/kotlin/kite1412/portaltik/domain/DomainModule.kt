package kite1412.portaltik.domain

import kite1412.portaltik.domain.usecase.CloseGateUseCase
import kite1412.portaltik.domain.usecase.EnterGateUseCase
import kite1412.portaltik.domain.usecase.ExitGateUseCase
import kite1412.portaltik.domain.usecase.GetMainCctvUseCase
import kite1412.portaltik.domain.usecase.GetMainGateUseCase
import kite1412.portaltik.domain.usecase.GetMainParkingQuotaUseCase
import kite1412.portaltik.domain.usecase.OpenGateUseCase
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
        OpenGateUseCase(gateRepository = get())
    }

    factory {
        CloseGateUseCase(gateRepository = get())
    }

    factory {
        EnterGateUseCase(gateRepository = get())
    }

    factory {
        ExitGateUseCase(gateRepository = get())
    }
}

val domainModule = module {
    includes(useCaseModule)
}