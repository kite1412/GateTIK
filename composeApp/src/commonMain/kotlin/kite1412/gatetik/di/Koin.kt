package kite1412.gatetik.di

import kite1412.gatetik.app.GateTikViewModel
import kite1412.gatetik.common.commonModule
import kite1412.gatetik.data.dataModule
import kite1412.gatetik.datastore.dataStoreModule
import kite1412.gatetik.domain.domainModule
import kite1412.gatetik.feature.monitoring.desktop.accesslogs.DesktopAccessLogsViewModel
import kite1412.gatetik.feature.monitoring.desktop.cctv.DesktopCctvViewModel
import kite1412.gatetik.feature.monitoring.desktop.dashboard.DesktopDashboardViewModel
import kite1412.gatetik.feature.monitoring.desktop.parking.DesktopParkingViewModel
import kite1412.gatetik.feature.monitoring.desktop.profile.DesktopProfileViewModel
import kite1412.gatetik.feature.monitoring.desktop.settings.DesktopSettingsViewModel
import kite1412.gatetik.feature.monitoring.desktop.usermanagement.DesktopUserManagementViewModel
import kite1412.gatetik.feature.monitoring.mobile.cctv.MobileCctvViewModel
import kite1412.gatetik.feature.monitoring.mobile.home.MobileHomeViewModel
import kite1412.gatetik.feature.monitoring.mobile.parking.MobileParkingViewModel
import kite1412.gatetik.feature.shared.authentication.AuthenticationViewModel
import kite1412.gatetik.feature.shared.profile.ProfileViewModel
import kite1412.gatetik.feature.student.gateaccess.GateAccessViewModel
import kite1412.gatetik.network.backend.backendModule
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
            dataStore = get(),
            updateProfileUseCase = get(),
            resetPasswordUseCase = get()
        )
    }
    viewModel {
        GateAccessViewModel(
            authentication = get(),
            getMainParkingQuotaUseCase = get(),
            locationService = get(),
            getMainGateUseCase = get(),
            accessGateUseCase = get()
        )
    }
}

private val mobileMonitoringViewModelModule = module {
    viewModel {
        MobileHomeViewModel(
            authentication = get(),
            getMainGateUseCase = get(),
            getCctvUseCase = get(),
            getMainParkingQuotaUseCase = get(),
            accessLogRepository = get(),
            accessGateUseCase = get()
        )
    }
    viewModel {
        MobileCctvViewModel(
            getCctvUseCase = get()
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
            dataStore = get(),
            getMainGateUseCase = get(),
            getMainParkingQuotaUseCase = get(),
            getCctvUseCase = get(),
            userRepository = get(),
            accessLogRepository = get(),
            accessGateUseCase = get()
        )
    }
    viewModel {
        DesktopCctvViewModel(
            authentication = get(),
            dataStore = get(),
            cctvRepository = get()
        )
    }
    viewModel {
        DesktopParkingViewModel(
            authentication = get(),
            dataStore = get(),
            getMainGateUseCase = get(),
            getMainParkingQuotaUseCase = get(),
            updateMainGateUseCase = get(),
            updateMainParkingQuotaUseCase = get(),
        )
    }
    viewModel {
        DesktopUserManagementViewModel(
            authentication = get(),
            dataStore = get(),
            userRepository = get()
        )
    }
    viewModel {
        DesktopAccessLogsViewModel(
            authentication = get(),
            dataStore = get(),
            accessLogRepository = get()
        )
    }
    viewModel {
        DesktopSettingsViewModel(
            authentication = get(),
            dataStore = get()
        )
    }
    viewModel {
        DesktopProfileViewModel(
            dataStore = get(),
            authentication = get(),
            updateProfileUseCase = get(),
            resetPasswordUseCase = get()
        )
    }
}

private val viewModelModule = module {
    includes(sharedViewModelModule, mobileMonitoringViewModelModule, desktopMonitoringViewModel)

    viewModel {
        GateTikViewModel(
            dataStore = get(),
            authentication = get(),
            versionChecker = get()
        )
    }
}

private val appModule = platformModule +
        dataStoreModule +
        domainModule +
        viewModelModule +
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