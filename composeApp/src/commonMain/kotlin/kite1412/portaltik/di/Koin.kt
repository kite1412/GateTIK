package kite1412.portaltik.di

import kite1412.portaltik.app.PortalTikViewModel
import kite1412.portaltik.datastore.di.dataStoreModule
import kite1412.portaltik.feature.admin.dashboard.DashboardViewModel
import kite1412.portaltik.feature.shared.authentication.AuthenticationViewModel
import kite1412.portaltik.network.mock.mockRemoteModule
import kite1412.portaltik.platformModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
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
        DashboardViewModel()
    }
}

private val appModule = platformModule + dataStoreModule + viewModelModule + mockRemoteModule

fun initKoin(
    extraModules: List<Module> = emptyList(),
    koinConfig: KoinApplication.() -> Unit = {}
) = startKoin {
    apply(koinConfig)
    modules(appModule + extraModules)
}