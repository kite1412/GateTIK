package kite1412.gatetik.di

import kite1412.gatetik.app.GateTikViewModel
import kite1412.gatetik.common.commonModule
import kite1412.gatetik.data.dataModule
import kite1412.gatetik.datastore.dataStoreModule
import kite1412.gatetik.domain.domainModule
import kite1412.gatetik.feature.monitoring.desktop.cctv.DesktopCctvViewModel
import kite1412.gatetik.feature.monitoring.desktop.dashboard.DesktopDashboardViewModel
import kite1412.gatetik.feature.monitoring.desktop.parking.DesktopParkingViewModel
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.DesktopUserManagementViewModel
import kite1412.gatetik.feature.monitoring.mobile.cctv.MobileCctvViewModel
import kite1412.gatetik.feature.monitoring.mobile.home.MobileHomeViewModel
import kite1412.gatetik.feature.monitoring.mobile.parking.MobileParkingViewModel
import kite1412.gatetik.feature.shared.authentication.AuthenticationViewModel
import kite1412.gatetik.feature.shared.profile.ProfileViewModel
import kite1412.gatetik.feature.student.gateaccess.GateAccessViewModel
import kite1412.gatetik.network.backend.backendModule
import kite1412.gatetik.network.mock.mockRemoteModule
import kite1412.gatetik.platformModule
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
    viewModel {
        DesktopUserManagementViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
}

private val viewModelModule = module {
    includes(sharedViewModelModule, mobileMonitoringViewModelModule, desktopMonitoringViewModel)

    viewModel {
        GateTikViewModel(
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