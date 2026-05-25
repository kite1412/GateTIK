package kite1412.portaltik.network.backend

import kite1412.portaltik.domain.Authentication
import org.koin.dsl.bind
import org.koin.dsl.module

val backendModule = module {
    single {
        BackendAuthentication()
    } bind Authentication::class
}