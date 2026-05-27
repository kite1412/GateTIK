package kite1412.portaltik.di

import kite1412.portaltik.app.PortalTikViewModel
import kite1412.portaltik.common.commonModule
import kite1412.portaltik.data.dataModule
import kite1412.portaltik.datastore.dataStoreModule
import kite1412.portaltik.domain.domainModule
import kite1412.portaltik.feature.admin.desktop.dashboard.DesktopAdminDashboardViewModel
import kite1412.portaltik.feature.admin.mobile.cctv.MobileAdminCctvViewModel
import kite1412.portaltik.feature.admin.mobile.gate.MobileAdminGateViewModel
import kite1412.portaltik.feature.admin.mobile.home.MobileAdminHomeViewModel
import kite1412.portaltik.feature.admin.mobile.parking.MobileAdminParkingViewModel
import kite1412.portaltik.feature.admin.mobile.profile.MobileAdminProfileViewModel
import kite1412.portaltik.feature.shared.authentication.AuthenticationViewModel
import kite1412.portaltik.network.backend.backendModule
import kite1412.portaltik.network.mock.mockRemoteModule
import kite1412.portaltik.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val mobileViewModelModule = module {
    viewModel {
        MobileAdminHomeViewModel(
            authentication = get(),
            getMainGateUseCase = get(),
            getMainCctvUseCase = get(),
            getMainParkingQuotaUseCase = get(),
            accessLogRepository = get(),
            openGateUseCase = get(),
            closeGateUseCase = get()
        )
    }
    viewModel {
        MobileAdminGateViewModel(
            getMainGateUseCase = get(),
            getMainParkingQuotaUseCase = get(),
            accessLogRepository = get(),
            openGateUseCase = get(),
            closeGateUseCase = get()
        )
    }
    viewModel {
        MobileAdminCctvViewModel(
            getMainCctvUseCase = get()
        )
    }
    viewModel {
        MobileAdminParkingViewModel(
            getMainParkingQuotaUseCase = get()
        )
    }
    viewModel {
        MobileAdminProfileViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
}

private val viewModelModule = module {
    includes(mobileViewModelModule)

    viewModel {
        AuthenticationViewModel(
            dataStore = get(),
            authentication = get(),
        )
    }
    viewModel {
        PortalTikViewModel(
            dataStore = get(),
            authentication = get(),
        )
    }
    viewModel {
        DesktopAdminDashboardViewModel()
    }
}

private val appModule = platformModule +
        dataStoreModule +
        domainModule +
        viewModelModule +
        mockRemoteModule +
        dataModule +
        backendModule +
        commonModule

fun initKoin(
    extraModules: List<Module> = emptyList(),
    koinConfig: KoinApplication.() -> Unit = {}
) = startKoin {
    apply(koinConfig)
    modules(appModule + extraModules)
}