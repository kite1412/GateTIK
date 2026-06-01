package kite1412.portaltik.di

import kite1412.portaltik.app.PortalTikViewModel
import kite1412.portaltik.common.commonModule
import kite1412.portaltik.data.dataModule
import kite1412.portaltik.datastore.dataStoreModule
import kite1412.portaltik.domain.domainModule
import kite1412.portaltik.feature.monitoring.desktop.cctv.DesktopCctvViewModel
import kite1412.portaltik.feature.monitoring.desktop.dashboard.DesktopDashboardViewModel
import kite1412.portaltik.feature.monitoring.desktop.parking.DesktopParkingViewModel
import kite1412.portaltik.feature.monitoring.mobile.cctv.MobileCctvViewModel
import kite1412.portaltik.feature.monitoring.mobile.home.MobileHomeViewModel
import kite1412.portaltik.feature.monitoring.mobile.parking.MobileParkingViewModel
import kite1412.portaltik.feature.shared.authentication.AuthenticationViewModel
import kite1412.portaltik.feature.shared.profile.ProfileViewModel
import kite1412.portaltik.feature.student.gateaccess.GateAccessViewModel
import kite1412.portaltik.network.backend.backendModule
import kite1412.portaltik.network.mock.mockRemoteModule
import kite1412.portaltik.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val sharedViewModelModule = module {
    viewModel {
        AuthenticationViewModel(
            dataStore = get(),
            authentication = get(),
        )
    }
    viewModel {
        ProfileViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
    viewModel {
        GateAccessViewModel(
            authentication = get(),
            getMainParkingQuotaUseCase = get(),
            locationService = get(),
            getMainGateUseCase = get(),
            openGateUseCase = get()
        )
    }
}

private val mobileMonitoringViewModelModule = module {
    viewModel {
        MobileHomeViewModel(
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
        MobileCctvViewModel(
            getMainCctvUseCase = get()
        )
    }
    viewModel {
        MobileParkingViewModel(
            getMainParkingQuotaUseCase = get()
        )
    }
}

private val desktopMonitoringViewModel = module {
    viewModel {
        DesktopDashboardViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
    viewModel {
        DesktopCctvViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
    viewModel {
        DesktopParkingViewModel(
            authentication = get(),
            dataStore = get(),
            getMainParkingQuotaUseCase = get()
        )
    }
}

private val viewModelModule = module {
    includes(sharedViewModelModule, mobileMonitoringViewModelModule, desktopMonitoringViewModel)

    viewModel {
        PortalTikViewModel(
            dataStore = get(),
            authentication = get()
        )
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