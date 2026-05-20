package kite1412.portaltik.di

import kite1412.portaltik.feature.authentication.AuthenticationViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule = module {
    viewModel {
        AuthenticationViewModel()
    }
}

private val appModule = viewModelModule

fun initKoin(
    extraModules: List<Module> = emptyList(),
    koinConfig: KoinApplication.() -> Unit = {}
) = startKoin {
    apply(koinConfig)
    modules(appModule + extraModules)
}